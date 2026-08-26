package com.keren.control.data.repository

import com.keren.control.data.remote.websocket.KerenWebSocket
import com.keren.control.domain.model.KerenEvent
import com.keren.control.domain.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val webSocket: KerenWebSocket
) : EventRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override val events: SharedFlow<KerenEvent> = webSocket.events

    private val _recent = MutableStateFlow<List<KerenEvent>>(emptyList())
    override val recentEvents: StateFlow<List<KerenEvent>> = _recent.asStateFlow()

    init {
        scope.launch {
            webSocket.events.collect { event ->
                _recent.update { current ->
                    (listOf(event) + current).take(200)
                }
            }
        }
    }
}
