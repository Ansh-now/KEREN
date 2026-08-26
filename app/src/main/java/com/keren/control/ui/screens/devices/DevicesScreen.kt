package com.keren.control.ui.screens.devices

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keren.control.domain.model.ConnectionState
import com.keren.control.domain.model.Device
import com.keren.control.domain.model.DeviceStatus
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.DevicesViewModel

@Composable
fun DevicesScreen(
    viewModel: DevicesViewModel = hiltViewModel()
) {
    val devices by viewModel.devices.collectAsState()
    val connection by viewModel.connection.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DEVICE REGISTERED",
                    color = KerenBlue,
                    fontSize = 18.sp
                )
                Text(
                    text = connection.state.name,
                    color = when (connection.state) {
                        ConnectionState.CONNECTED -> KerenGreen
                        ConnectionState.ERROR -> KerenRed
                        ConnectionState.CONNECTING, ConnectionState.RECONNECTING -> KerenAmber
                        else -> KerenGrey
                    },
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (connection.state != ConnectionState.CONNECTED && devices.isEmpty()) {
                Text(
                    text = "Not connected to KEREN Core.\nOpen Settings → set Core URL → CONNECT.",
                    color = KerenTextDim,
                    fontSize = 13.sp
                )
            } else if (devices.isEmpty()) {
                Text(
                    text = "No devices registered yet.",
                    color = KerenTextDim,
                    fontSize = 13.sp
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(devices, key = { it.id }) { device ->
                        DeviceCard(device)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { /* Add Device flow — P1 */ },
            containerColor = KerenBlue,
            contentColor = KerenBlack,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Device")
        }
    }
}

@Composable
private fun DeviceCard(device: Device) {
    val statusColor = when (device.status) {
        DeviceStatus.ONLINE -> OnlineGreen
        DeviceStatus.OFFLINE, DeviceStatus.ERROR -> OfflineRed
        DeviceStatus.STALE, DeviceStatus.CONNECTING -> KerenAmber
        DeviceStatus.UNKNOWN -> KerenGrey
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KerenSurface, RoundedCornerShape(8.dp))
            .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = device.name, color = KerenText, fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = device.status.name, color = statusColor, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "TYPE: ${device.type}", color = KerenGrey, fontSize = 12.sp)
        device.address?.let {
            Text(text = "ADDRESS: $it", color = KerenGrey, fontSize = 12.sp)
        }
        if (device.capabilities.isNotEmpty()) {
            Text(
                text = "CAPABILITIES: ${device.capabilities.joinToString(", ")}",
                color = KerenGrey,
                fontSize = 12.sp
            )
        }
        device.currentTaskId?.let {
            Text(text = "CURRENT TASK: $it", color = KerenAmber, fontSize = 12.sp)
        }
        device.telemetry?.let { t ->
            val parts = buildList {
                t.cpuPercent?.let { add("CPU ${it.toInt()}%") }
                t.ramPercent?.let { add("RAM ${it.toInt()}%") }
                t.batteryPercent?.let { add("BAT ${it.toInt()}%") }
            }
            if (parts.isNotEmpty()) {
                Text(text = parts.joinToString("  "), color = KerenTextDim, fontSize = 11.sp)
            }
        }
    }
}
