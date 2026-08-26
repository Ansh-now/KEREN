package com.keren.control.domain.model

/**
 * Core models for KEREN Nervous System Live View.
 * Every visual element must be driven by real events from the Event Bus.
 */

enum class NodeState {
    IDLE,
    RECEIVING,
    PROCESSING,
    SENDING,
    ERROR,
    OFFLINE
}

enum class NodeType {
    USER,
    CORE,
    PLANNER,
    MEMORY,
    ROUTER,
    VISION,
    VERIFIER,
    DEVICE,
    WORKER,
    APP,
    TOOL
}

data class KerenNode(
    val id: String,
    val label: String,
    val type: NodeType,
    val state: NodeState = NodeState.IDLE,
    val x: Float = 0.5f,          // relative 0..1
    val y: Float = 0.5f,
    val isOnline: Boolean = true,
    val telemetry: NodeTelemetry? = null
)

data class NodeTelemetry(
    val cpu: Float? = null,
    val ram: Float? = null,
    val battery: Float? = null,
    val temperature: Float? = null,
    val latencyMs: Int? = null,
    val currentAction: String? = null,
    val capabilities: List<String> = emptyList()
)

data class KerenEdge(
    val id: String,
    val fromId: String,
    val toId: String,
    val isActive: Boolean = false,
    val lastLatencyMs: Int? = null
)

enum class PacketDirection {
    FORWARD,   // request / command
    BACKWARD   // result / response
}

data class LivePacket(
    val id: String,
    val edgeId: String,
    val direction: PacketDirection,
    val progress: Float = 0f,   // 0..1 along the edge
    val color: PacketColor = PacketColor.BLUE,
    val taskId: String? = null
)

enum class PacketColor {
    BLUE,    // outbound
    GREEN,   // success / return
    RED,     // error
    AMBER    // warning
}

data class NervousEvent(
    val timestamp: String,
    val message: String,
    val level: EventLevel = EventLevel.INFO,
    val taskId: String? = null,
    val nodeId: String? = null
)

enum class EventLevel {
    INFO, SUCCESS, WARNING, ERROR
}

/**
 * Snapshot of the entire nervous system at a moment in time.
 * UI should render from this + animated packets.
 */
data class NervousSnapshot(
    val nodes: List<KerenNode> = emptyList(),
    val edges: List<KerenEdge> = emptyList(),
    val packets: List<LivePacket> = emptyList(),
    val events: List<NervousEvent> = emptyList(),
    val focusedTaskId: String? = null
)
