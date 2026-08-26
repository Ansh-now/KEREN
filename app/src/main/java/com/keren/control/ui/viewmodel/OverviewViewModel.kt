package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.keren.control.domain.model.ConnectionState
import com.keren.control.domain.model.DeviceStatus
import com.keren.control.domain.model.KerenEvent
import com.keren.control.domain.model.TaskStatus
import com.keren.control.domain.repository.ConnectionRepository
import com.keren.control.domain.repository.DeviceRepository
import com.keren.control.domain.repository.EventRepository
import com.keren.control.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    connectionRepository: ConnectionRepository,
    deviceRepository: DeviceRepository,
    taskRepository: TaskRepository,
    eventRepository: EventRepository
) : ViewModel() {

    val connection = connectionRepository.connection
    val devices = deviceRepository.devices
    val tasks = taskRepository.tasks
    val recentEvents: StateFlow<List<KerenEvent>> = eventRepository.recentEvents

    fun coreLabel(): String {
        return when (connection.value.state) {
            ConnectionState.CONNECTED -> "CONNECTED"
            ConnectionState.CONNECTING -> "CONNECTING"
            ConnectionState.RECONNECTING -> "RECONNECTING"
            ConnectionState.ERROR -> "ERROR"
            ConnectionState.DISCONNECTED -> "DISCONNECTED"
        }
    }

    fun onlineDeviceCount(): Int =
        devices.value.count { it.status == DeviceStatus.ONLINE }

    fun offlineDeviceCount(): Int =
        devices.value.count { it.status == DeviceStatus.OFFLINE || it.status == DeviceStatus.STALE }

    fun executingCount(): Int =
        tasks.value.count { it.status == TaskStatus.EXECUTING || it.status == TaskStatus.DISPATCHED }

    fun queuedCount(): Int =
        tasks.value.count {
            it.status == TaskStatus.QUEUED ||
                it.status == TaskStatus.RECEIVED ||
                it.status == TaskStatus.PLANNING ||
                it.status == TaskStatus.ROUTING
        }

    fun completedCount(): Int =
        tasks.value.count { it.status == TaskStatus.COMPLETED }

    fun failedCount(): Int =
        tasks.value.count {
            it.status == TaskStatus.FAILED || it.status == TaskStatus.TIMEOUT
        }
}
