package com.keren.control.data.mapper

import com.keren.control.data.remote.dto.DeviceDto
import com.keren.control.data.remote.dto.TaskDto
import com.keren.control.data.remote.dto.TelemetryDto
import com.keren.control.domain.model.Device
import com.keren.control.domain.model.DeviceStatus
import com.keren.control.domain.model.DeviceTelemetry
import com.keren.control.domain.model.Task
import com.keren.control.domain.model.TaskStatus

fun DeviceDto.toDomain(): Device {
    return Device(
        id = id,
        name = name ?: id,
        type = type ?: "unknown",
        status = status.toDeviceStatus(),
        address = address,
        port = port,
        capabilities = capabilities.orEmpty(),
        agentVersion = agentVersion,
        protocolVersion = protocolVersion,
        lastSeen = lastSeen,
        currentTaskId = currentTaskId,
        telemetry = telemetry?.toDomain()
    )
}

fun TelemetryDto.toDomain(): DeviceTelemetry {
    return DeviceTelemetry(
        cpuPercent = cpuPercent,
        ramPercent = ramPercent,
        diskPercent = diskPercent,
        batteryPercent = batteryPercent,
        temperatureC = temperatureC,
        networkLatencyMs = networkLatencyMs
    )
}

fun TaskDto.toDomain(): Task {
    val resolvedId = taskId ?: id ?: "unknown"
    return Task(
        id = resolvedId,
        sourceNodeId = sourceNodeId,
        targetNodeId = targetNodeId,
        type = type ?: "shell",
        command = command ?: "",
        priority = priority ?: "normal",
        status = status.toTaskStatus(),
        createdAt = createdAt,
        startedAt = startedAt,
        completedAt = completedAt,
        exitCode = exitCode,
        stdout = stdout,
        stderr = stderr,
        error = error
    )
}

private fun String?.toDeviceStatus(): DeviceStatus {
    return when (this?.uppercase()) {
        "ONLINE" -> DeviceStatus.ONLINE
        "OFFLINE" -> DeviceStatus.OFFLINE
        "STALE" -> DeviceStatus.STALE
        "CONNECTING" -> DeviceStatus.CONNECTING
        "ERROR" -> DeviceStatus.ERROR
        else -> DeviceStatus.UNKNOWN
    }
}

private fun String?.toTaskStatus(): TaskStatus {
    return when (this?.uppercase()) {
        "RECEIVED" -> TaskStatus.RECEIVED
        "PLANNING" -> TaskStatus.PLANNING
        "QUEUED" -> TaskStatus.QUEUED
        "ROUTING" -> TaskStatus.ROUTING
        "DISPATCHED" -> TaskStatus.DISPATCHED
        "EXECUTING", "RUNNING" -> TaskStatus.EXECUTING
        "COMPLETED", "SUCCESS" -> TaskStatus.COMPLETED
        "FAILED", "ERROR" -> TaskStatus.FAILED
        "CANCELLED", "CANCELED" -> TaskStatus.CANCELLED
        "TIMEOUT" -> TaskStatus.TIMEOUT
        else -> TaskStatus.UNKNOWN
    }
}
