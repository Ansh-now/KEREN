package com.keren.control.domain.model

/**
 * Versioned event envelope from KEREN Core WebSocket / Event Bus.
 * UI must never parse raw JSON — always use typed events.
 */
data class KerenEvent(
    val protocolVersion: String = "0.6",
    val eventId: String,
    val event: String,
    val timestamp: String,
    val taskId: String? = null,
    val sourceNodeId: String? = null,
    val targetNodeId: String? = null,
    val deviceId: String? = null,
    val payload: Map<String, Any?> = emptyMap()
)

object KerenEventType {
    const val DEVICE_CONNECTED = "device.connected"
    const val DEVICE_DISCONNECTED = "device.disconnected"
    const val DEVICE_HEARTBEAT = "device.heartbeat"
    const val DEVICE_TELEMETRY = "device.telemetry"

    const val TASK_CREATED = "task.created"
    const val TASK_QUEUED = "task.queued"
    const val TASK_PLANNING = "task.planning"
    const val TASK_ROUTED = "task.routed"
    const val TASK_DISPATCHED = "task.dispatched"
    const val TASK_STARTED = "task.started"
    const val TASK_STDOUT = "task.stdout"
    const val TASK_STDERR = "task.stderr"
    const val TASK_COMPLETED = "task.completed"
    const val TASK_FAILED = "task.failed"
    const val TASK_CANCELLED = "task.cancelled"
    const val TASK_TIMEOUT = "task.timeout"
}
