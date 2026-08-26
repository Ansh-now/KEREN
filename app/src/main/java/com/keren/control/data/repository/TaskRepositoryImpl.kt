package com.keren.control.data.repository

import com.keren.control.data.mapper.toDomain
import com.keren.control.data.remote.api.KerenApi
import com.keren.control.data.remote.dto.CreateTaskDto
import com.keren.control.data.remote.websocket.KerenWebSocket
import com.keren.control.domain.model.CreateTaskRequest
import com.keren.control.domain.model.KerenEventType
import com.keren.control.domain.model.Task
import com.keren.control.domain.model.TaskStatus
import com.keren.control.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val api: KerenApi,
    private val webSocket: KerenWebSocket
) : TaskRepository {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    override val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    override val queued: StateFlow<List<Task>> = _tasks
        .map { list -> list.filter { it.status == TaskStatus.QUEUED || it.status == TaskStatus.RECEIVED || it.status == TaskStatus.PLANNING || it.status == TaskStatus.ROUTING } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val executing: StateFlow<List<Task>> = _tasks
        .map { list -> list.filter { it.status == TaskStatus.EXECUTING || it.status == TaskStatus.DISPATCHED } }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val history: StateFlow<List<Task>> = _tasks
        .map { list ->
            list.filter {
                it.status == TaskStatus.COMPLETED ||
                    it.status == TaskStatus.FAILED ||
                    it.status == TaskStatus.CANCELLED ||
                    it.status == TaskStatus.TIMEOUT
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    init {
        scope.launch {
            webSocket.events.collect { event ->
                when (event.event) {
                    KerenEventType.TASK_CREATED,
                    KerenEventType.TASK_QUEUED,
                    KerenEventType.TASK_PLANNING,
                    KerenEventType.TASK_ROUTED,
                    KerenEventType.TASK_DISPATCHED,
                    KerenEventType.TASK_STARTED,
                    KerenEventType.TASK_COMPLETED,
                    KerenEventType.TASK_FAILED,
                    KerenEventType.TASK_CANCELLED,
                    KerenEventType.TASK_TIMEOUT -> {
                        runCatching { refresh() }
                    }
                    KerenEventType.TASK_STDOUT,
                    KerenEventType.TASK_STDERR -> {
                        // Partial update: refresh for simplicity in V0.6
                        runCatching { refresh() }
                    }
                }
            }
        }
    }

    override suspend fun refresh() {
        val list = api.getTasks().map { it.toDomain() }
        _tasks.value = list
    }

    override suspend fun createTask(request: CreateTaskRequest): Result<Task> {
        return runCatching {
            val dto = CreateTaskDto(
                command = request.command,
                type = request.type,
                targetNodeId = request.targetNodeId,
                priority = request.priority,
                timeoutSeconds = request.timeoutSeconds
            )
            val created = api.createTask(dto).toDomain()
            // Optimistic local insert until next refresh
            _tasks.updateUnique(created)
            created
        }
    }

    override suspend fun cancelTask(taskId: String): Result<Unit> {
        return runCatching {
            api.cancelTask(taskId)
            refresh()
        }
    }

    override suspend fun getTask(taskId: String): Task? {
        return _tasks.value.find { it.id == taskId }
            ?: runCatching { api.getTask(taskId).toDomain() }.getOrNull()
    }

    private fun MutableStateFlow<List<Task>>.updateUnique(task: Task) {
        value = (listOf(task) + value.filter { it.id != task.id })
    }
}
