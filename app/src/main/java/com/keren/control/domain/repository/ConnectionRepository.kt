package com.keren.control.domain.repository

import com.keren.control.domain.model.ConnectionInfo
import com.keren.control.domain.model.CoreConfig
import kotlinx.coroutines.flow.StateFlow

interface ConnectionRepository {
    val connection: StateFlow<ConnectionInfo>
    val config: StateFlow<CoreConfig>

    fun updateConfig(config: CoreConfig)
    suspend fun connect()
    suspend fun disconnect()
    suspend fun resync()
}
