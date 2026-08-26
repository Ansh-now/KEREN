package com.keren.control.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HealthDto(
    @SerializedName("status") val status: String? = null,
    @SerializedName("version") val version: String? = null,
    @SerializedName("protocol_version") val protocolVersion: String? = null
)

data class DeviceDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("port") val port: Int? = null,
    @SerializedName("capabilities") val capabilities: List<String>? = null,
    @SerializedName("agent_version") val agentVersion: String? = null,
    @SerializedName("protocol_version") val protocolVersion: String? = null,
    @SerializedName("last_seen") val lastSeen: String? = null,
    @SerializedName("current_task_id") val currentTaskId: String? = null,
    @SerializedName("telemetry") val telemetry: TelemetryDto? = null
)

data class TelemetryDto(
    @SerializedName("cpu_percent") val cpuPercent: Float? = null,
    @SerializedName("ram_percent") val ramPercent: Float? = null,
    @SerializedName("disk_percent") val diskPercent: Float? = null,
    @SerializedName("battery_percent") val batteryPercent: Float? = null,
    @SerializedName("temperature_c") val temperatureC: Float? = null,
    @SerializedName("network_latency_ms") val networkLatencyMs: Int? = null
)

data class TaskDto(
    @SerializedName("task_id") val taskId: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("source_node_id") val sourceNodeId: String? = null,
    @SerializedName("target_node_id") val targetNodeId: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("command") val command: String? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("started_at") val startedAt: String? = null,
    @SerializedName("completed_at") val completedAt: String? = null,
    @SerializedName("exit_code") val exitCode: Int? = null,
    @SerializedName("stdout") val stdout: String? = null,
    @SerializedName("stderr") val stderr: String? = null,
    @SerializedName("error") val error: String? = null
)

data class CreateTaskDto(
    @SerializedName("command") val command: String,
    @SerializedName("type") val type: String = "shell",
    @SerializedName("target_node_id") val targetNodeId: String? = null,
    @SerializedName("priority") val priority: String = "normal",
    @SerializedName("timeout_seconds") val timeoutSeconds: Int? = 300
)

data class EventDto(
    @SerializedName("protocol_version") val protocolVersion: String? = null,
    @SerializedName("event_id") val eventId: String? = null,
    @SerializedName("event") val event: String? = null,
    @SerializedName("timestamp") val timestamp: String? = null,
    @SerializedName("task_id") val taskId: String? = null,
    @SerializedName("source_node_id") val sourceNodeId: String? = null,
    @SerializedName("target_node_id") val targetNodeId: String? = null,
    @SerializedName("device_id") val deviceId: String? = null,
    @SerializedName("payload") val payload: Map<String, Any?>? = null
)
