package com.keren.control.domain.model

/**
 * Real device model — values must come from KEREN Core.
 * Never invent CPU/RAM/status on the client.
 */
enum class DeviceStatus {
    ONLINE,
    OFFLINE,
    STALE,
    CONNECTING,
    ERROR,
    UNKNOWN
}

data class Device(
    val id: String,
    val name: String,
    val type: String,
    val status: DeviceStatus,
    val address: String? = null,
    val port: Int? = null,
    val capabilities: List<String> = emptyList(),
    val agentVersion: String? = null,
    val protocolVersion: String? = null,
    val lastSeen: String? = null,
    val currentTaskId: String? = null,
    val telemetry: DeviceTelemetry? = null
)

data class DeviceTelemetry(
    val cpuPercent: Float? = null,
    val ramPercent: Float? = null,
    val diskPercent: Float? = null,
    val batteryPercent: Float? = null,
    val temperatureC: Float? = null,
    val networkLatencyMs: Int? = null
)
