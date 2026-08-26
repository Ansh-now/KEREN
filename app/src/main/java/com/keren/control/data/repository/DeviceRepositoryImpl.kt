package com.keren.control.data.repository

import com.keren.control.data.mapper.toDomain
import com.keren.control.data.remote.api.KerenApi
import com.keren.control.data.remote.websocket.KerenWebSocket
import com.keren.control.domain.model.Device
import com.keren.control.domain.model.DeviceStatus
import com.keren.control.domain.model.KerenEventType
import com.keren.control.domain.repository.DeviceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val api: KerenApi,
    private val webSocket: KerenWebSocket
) : DeviceRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    override val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    init {
        scope.launch {
            webSocket.events.collect { event ->
                when (event.event) {
                    KerenEventType.DEVICE_CONNECTED,
                    KerenEventType.DEVICE_HEARTBEAT,
                    KerenEventType.DEVICE_TELEMETRY -> {
                        // Full refresh keeps single source of truth simple for V0.6
                        runCatching { refresh() }
                    }
                    KerenEventType.DEVICE_DISCONNECTED -> {
                        val id = event.deviceId ?: return@collect
                        _devices.update { list ->
                            list.map {
                                if (it.id == id) it.copy(status = DeviceStatus.OFFLINE) else it
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun refresh() {
        val list = api.getDevices().map { it.toDomain() }
        _devices.value = list
    }

    override suspend fun getDevice(id: String): Device? {
        return _devices.value.find { it.id == id }
            ?: runCatching { api.getDevice(id).toDomain() }.getOrNull()
    }
}
