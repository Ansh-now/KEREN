package com.keren.control.domain.model

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    RECONNECTING,
    ERROR
}

data class CoreConfig(
    val httpBaseUrl: String = "",
    val wsUrl: String = "",
    val authToken: String? = null,
    val protocolVersion: String = "0.6"
)

data class ConnectionInfo(
    val state: ConnectionState = ConnectionState.DISCONNECTED,
    val lastError: String? = null,
    val coreVersion: String? = null,
    val connectedAt: String? = null
)
