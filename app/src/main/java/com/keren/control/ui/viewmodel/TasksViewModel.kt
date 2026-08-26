package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keren.control.domain.model.CreateTaskRequest
import com.keren.control.domain.model.Task
import com.keren.control.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val queued: StateFlow<List<Task>> = taskRepository.queued
    val executing: StateFlow<List<Task>> = taskRepository.executing
    val history: StateFlow<List<Task>> = taskRepository.history

    fun refresh() {
        viewModelScope.launch {
            runCatching { taskRepository.refresh() }
        }
    }

    fun submitCommand(command: String, targetNodeId: String? = null) {
        viewModelScope.launch {
            taskRepository.createTask(
                CreateTaskRequest(
                    command = command,
                    targetNodeId = targetNodeId
                )
            )
        }
    }
}
