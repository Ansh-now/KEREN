package com.keren.control.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keren.control.domain.model.Device
import com.keren.control.domain.repository.ConnectionRepository
import com.keren.control.domain.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevicesViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    connectionRepository: ConnectionRepository
) : ViewModel() {

    val devices: StateFlow<List<Device>> = deviceRepository.devices
    val connection = connectionRepository.connection

    fun refresh() {
        viewModelScope.launch {
            runCatching { deviceRepository.refresh() }
        }
    }
}
