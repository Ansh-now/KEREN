package com.keren.control.data.connection

import com.keren.control.data.remote.api.KerenApi
import com.keren.control.data.remote.websocket.KerenWebSocket
import com.keren.control.data.remote.websocket.WsConnectionEvent
import com.keren.control.domain.model.ConnectionInfo
import com.keren.control.domain.model.ConnectionState
import com.keren.control.domain.model.CoreConfig
import com.keren.control.domain.repository.ConnectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central connection authority for the Android client.
 *
 * Responsibilities:
 * - CONNECTING / CONNECTED / DISCONNECTED / RECONNECTING / ERROR
 * - Exponential backoff reconnect
 * - On reconnect: caller should resync devices + tasks (via repositories)
 */
@Singleton
class ConnectionManager @Inject constructor(
    private val api: KerenApi,
    private val webSocket: KerenWebSocket
) : ConnectionRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _config = MutableStateFlow(CoreConfig())
    override val config: StateFlow<CoreConfig> = _config.asStateFlow()

    private val _connection = MutableStateFlow(ConnectionInfo())
    override val connection: StateFlow<ConnectionInfo> = _connection.asStateFlow()

    private var reconnectJob: Job? = null
    private var observeJob: Job? = null
    private var attempt = 0

    init {
        observeWebSocket()
    }

    override fun updateConfig(config: CoreConfig) {
        _config.value = config
    }

    override suspend fun connect() {
        val cfg = _config.value
        if (cfg.httpBaseUrl.isBlank() && cfg.wsUrl.isBlank()) {
            _connection.value = ConnectionInfo(
                state = ConnectionState.ERROR,
                lastError = "Core URL not configured"
            )
            return
        }

        _connection.update { it.copy(state = ConnectionState.CONNECTING, lastError = null) }

        // Health check via REST when possible
        try {
            if (cfg.httpBaseUrl.isNotBlank()) {
                val health = api.health()
                _connection.update {
                    it.copy(coreVersion = health.version)
                }
            }
        } catch (e: Exception) {
            // Health optional on first connect; WS still attempted
        }

        val ws = cfg.wsUrl.ifBlank {
            // Derive ws from http if needed
            cfg.httpBaseUrl
                .replace("https://", "wss://")
                .replace("http://", "ws://")
                .trimEnd('/') + "/v0.6/ws"
        }

        webSocket.connect(ws, cfg.authToken)
    }

    override suspend fun disconnect() {
        reconnectJob?.cancel()
        webSocket.disconnect()
        _connection.value = ConnectionInfo(state = ConnectionState.DISCONNECTED)
        attempt = 0
    }

    override suspend fun resync() {
        // Repositories listen and call refresh() when CONNECTED.
        // This method is the explicit hook after reconnect.
        connect()
    }

    private fun observeWebSocket() {
        observeJob?.cancel()
        observeJob = scope.launch {
            webSocket.connectionEvents.collect { event ->
                when (event) {
                    is WsConnectionEvent.Opened -> {
                        attempt = 0
                        _connection.update {
                            it.copy(
                                state = ConnectionState.CONNECTED,
                                lastError = null,
                                connectedAt = java.time.Instant.now().toString()
                            )
                        }
                    }
                    is WsConnectionEvent.Closed -> {
                        _connection.update {
                            it.copy(state = ConnectionState.DISCONNECTED)
                        }
                        scheduleReconnect()
                    }
                    is WsConnectionEvent.Failed -> {
                        _connection.update {
                            it.copy(
                                state = ConnectionState.ERROR,
                                lastError = event.message
                            )
                        }
                        scheduleReconnect()
                    }
                }
            }
        }
    }

    private fun scheduleReconnect() {
        reconnectJob?.cancel()
        reconnectJob = scope.launch {
            if (!isActive) return@launch
            _connection.update { it.copy(state = ConnectionState.RECONNECTING) }
            attempt++
            val delayMs = (1000L * (1 shl attempt.coerceAtMost(5))).coerceAtMost(30_000L)
            delay(delayMs)
            connect()
        }
    }
}
