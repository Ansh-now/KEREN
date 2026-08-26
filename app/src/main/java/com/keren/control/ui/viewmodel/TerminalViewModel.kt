package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keren.control.domain.model.ConnectionState
import com.keren.control.domain.model.CreateTaskRequest
import com.keren.control.domain.model.KerenEventType
import com.keren.control.domain.repository.ConnectionRepository
import com.keren.control.domain.repository.EventRepository
import com.keren.control.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TerminalViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val eventRepository: EventRepository,
    connectionRepository: ConnectionRepository
) : ViewModel() {

    val connection = connectionRepository.connection

    private val _lines = MutableStateFlow(
        listOf(
            "KEREN TERMINAL",
            "────────────────────────────────────────",
            "Connect to Core in Settings, then type a command."
        )
    )
    val lines: StateFlow<List<String>> = _lines.asStateFlow()

    private var activeTaskId: String? = null

    init {
        viewModelScope.launch {
            eventRepository.events.collect { event ->
                when (event.event) {
                    KerenEventType.TASK_STDOUT -> {
                        if (activeTaskId == null || event.taskId == activeTaskId) {
                            val chunk = event.payload["data"]?.toString()
                                ?: event.payload["line"]?.toString()
                                ?: event.payload["stdout"]?.toString()
                            if (!chunk.isNullOrBlank()) append(chunk)
                        }
                    }
                    KerenEventType.TASK_STDERR -> {
                        if (activeTaskId == null || event.taskId == activeTaskId) {
                            val chunk = event.payload["data"]?.toString()
                                ?: event.payload["line"]?.toString()
                                ?: event.payload["stderr"]?.toString()
                            if (!chunk.isNullOrBlank()) append("[stderr] $chunk")
                        }
                    }
                    KerenEventType.TASK_STARTED -> {
                        if (event.taskId != null && event.taskId == activeTaskId) {
                            append("[${event.timestamp}] started on ${event.targetNodeId ?: "?"}")
                        }
                    }
                    KerenEventType.TASK_COMPLETED -> {
                        if (event.taskId != null && event.taskId == activeTaskId) {
                            val code = event.payload["exit_code"]?.toString() ?: "?"
                            append("[${event.timestamp}] completed exit=$code")
                            activeTaskId = null
                        }
                    }
                    KerenEventType.TASK_FAILED, KerenEventType.TASK_TIMEOUT -> {
                        if (event.taskId != null && event.taskId == activeTaskId) {
                            val err = event.payload["error"]?.toString() ?: event.event
                            append("[${event.timestamp}] FAILED: $err")
                            activeTaskId = null
                        }
                    }
                }
            }
        }
    }

    fun submit(command: String) {
        val cmd = command.trim()
        if (cmd.isEmpty()) return

        val state = connection.value.state
        if (state != ConnectionState.CONNECTED) {
            append("Not connected to KEREN Core. Open Settings → CONNECT.")
            return
        }

        append("\$ $cmd")
        viewModelScope.launch {
            val result = taskRepository.createTask(CreateTaskRequest(command = cmd))
            result.onSuccess { task ->
                activeTaskId = task.id
                append("[task ${task.id}] ${task.status.name}")
            }.onFailure { e ->
                append("ERROR: ${e.message ?: "create task failed"}")
            }
        }
    }

    private fun append(line: String) {
        _lines.update { current -> (current + line).takeLast(500) }
    }
}
