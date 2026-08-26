package com.keren.control.domain.repository

import com.keren.control.domain.model.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<List<Device>>
    suspend fun refresh()
    suspend fun getDevice(id: String): Device?
}
