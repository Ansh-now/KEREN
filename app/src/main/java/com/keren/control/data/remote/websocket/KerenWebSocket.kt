package com.keren.control.data.remote.websocket

import com.google.gson.Gson
import com.keren.control.data.remote.dto.EventDto
import com.keren.control.domain.model.KerenEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

/**
 * WebSocket client for KEREN Core event bus.
 * Emits only parsed, typed KerenEvent — never raw strings to UI.
 */
@Singleton
class KerenWebSocket @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) {
    private var socket: WebSocket? = null

    private val _events = MutableSharedFlow<KerenEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<KerenEvent> = _events.asSharedFlow()

    private val _connectionEvents = MutableSharedFlow<WsConnectionEvent>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionEvents: SharedFlow<WsConnectionEvent> = _connectionEvents.asSharedFlow()

    fun connect(wsUrl: String, authToken: String? = null) {
        disconnect()
        val builder = Request.Builder().url(wsUrl)
        if (!authToken.isNullOrBlank()) {
            builder.header("Authorization", "Bearer $authToken")
        }
        socket = client.newWebSocket(builder.build(), listener)
    }

    fun disconnect() {
        socket?.close(1000, "client disconnect")
        socket = null
    }

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            _connectionEvents.tryEmit(WsConnectionEvent.Opened)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                val dto = gson.fromJson(text, EventDto::class.java)
                val event = dto.toDomain()
                if (event != null) {
                    _events.tryEmit(event)
                }
            } catch (_: Exception) {
                // Invalid event — drop, do not crash UI
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            _connectionEvents.tryEmit(WsConnectionEvent.Closed(code, reason))
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            _connectionEvents.tryEmit(WsConnectionEvent.Failed(t.message ?: "WebSocket failure"))
        }
    }

    private fun EventDto.toDomain(): KerenEvent? {
        val id = eventId ?: return null
        val type = event ?: return null
        return KerenEvent(
            protocolVersion = protocolVersion ?: "0.6",
            eventId = id,
            event = type,
            timestamp = timestamp ?: "",
            taskId = taskId,
            sourceNodeId = sourceNodeId,
            targetNodeId = targetNodeId,
            deviceId = deviceId,
            payload = payload ?: emptyMap()
        )
    }
}

sealed class WsConnectionEvent {
    data object Opened : WsConnectionEvent()
    data class Closed(val code: Int, val reason: String) : WsConnectionEvent()
    data class Failed(val message: String) : WsConnectionEvent()
}
