package com.keren.control.domain.model

/**
 * Real task model — lifecycle driven by KEREN Core events.
 */
enum class TaskStatus {
    RECEIVED,
    PLANNING,
    QUEUED,
    ROUTING,
    DISPATCHED,
    EXECUTING,
    COMPLETED,
    FAILED,
    CANCELLED,
    TIMEOUT,
    UNKNOWN
}

data class Task(
    val id: String,
    val sourceNodeId: String? = null,
    val targetNodeId: String? = null,
    val type: String = "shell",
    val command: String,
    val priority: String = "normal",
    val status: TaskStatus = TaskStatus.RECEIVED,
    val createdAt: String? = null,
    val startedAt: String? = null,
    val completedAt: String? = null,
    val exitCode: Int? = null,
    val stdout: String? = null,
    val stderr: String? = null,
    val error: String? = null
)

data class CreateTaskRequest(
    val command: String,
    val type: String = "shell",
    val targetNodeId: String? = null,
    val priority: String = "normal",
    val timeoutSeconds: Int? = 300
)
