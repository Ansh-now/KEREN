package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keren.control.domain.model.ConnectionInfo
import com.keren.control.domain.model.CoreConfig
import com.keren.control.domain.repository.ConnectionRepository
import com.keren.control.domain.repository.DeviceRepository
import com.keren.control.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val connectionRepository: ConnectionRepository,
    private val deviceRepository: DeviceRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    val connection: StateFlow<ConnectionInfo> = connectionRepository.connection
    val config: StateFlow<CoreConfig> = connectionRepository.config

    fun updateConfig(httpBaseUrl: String, wsUrl: String, authToken: String?) {
        connectionRepository.updateConfig(
            CoreConfig(
                httpBaseUrl = httpBaseUrl.trim().trimEnd('/'),
                wsUrl = wsUrl.trim(),
                authToken = authToken?.takeIf { it.isNotBlank() }
            )
        )
    }

    fun connect() {
        viewModelScope.launch {
            connectionRepository.connect()
            // After connect attempt, try resync if connected path succeeds later via events
            runCatching {
                deviceRepository.refresh()
                taskRepository.refresh()
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            connectionRepository.disconnect()
        }
    }

    fun resync() {
        viewModelScope.launch {
            connectionRepository.resync()
            runCatching {
                deviceRepository.refresh()
                taskRepository.refresh()
            }
        }
    }
}
