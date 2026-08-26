package com.keren.control.domain.repository

import com.keren.control.domain.model.CreateTaskRequest
import com.keren.control.domain.model.Task
import kotlinx.coroutines.flow.StateFlow

interface TaskRepository {
    val tasks: StateFlow<List<Task>>
    val queued: StateFlow<List<Task>>
    val executing: StateFlow<List<Task>>
    val history: StateFlow<List<Task>>

    suspend fun refresh()
    suspend fun createTask(request: CreateTaskRequest): Result<Task>
    suspend fun cancelTask(taskId: String): Result<Unit>
    suspend fun getTask(taskId: String): Task?
}
