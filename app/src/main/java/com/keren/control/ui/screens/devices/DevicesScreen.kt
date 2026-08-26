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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

data class DeviceUi(
    val name: String,
    val type: String,
    val status: String,
    val ip: String,
    val capabilities: List<String>,
    val currentTask: String?
)

@Composable
fun DevicesScreen() {
    val devices = listOf(
        DeviceUi("PC-NODE-01", "PC", "ONLINE", "192.168.1.10", listOf("Terminal", "Python", "Docker"), null),
        DeviceUi("PHONE", "Phone", "ONLINE", "192.168.1.25", listOf("Control", "Observe"), null)
    )

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
            Text(
                text = "DEVICE REGISTERED",
                color = KerenBlue,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(devices) { device ->
                    DeviceCard(device)
                }
            }
        }

        FloatingActionButton(
            onClick = { /* TODO: Add Device */ },
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
private fun DeviceCard(device: DeviceUi) {
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
                    .background(if (device.status == "ONLINE") OnlineGreen else OfflineRed)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = device.name, color = KerenText, fontSize = 15.sp)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = device.status, color = if (device.status == "ONLINE") KerenGreen else KerenRed, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "TYPE: ${device.type}", color = KerenGrey, fontSize = 12.sp)
        Text(text = "IP: ${device.ip}", color = KerenGrey, fontSize = 12.sp)
        Text(
            text = "CAPABILITIES: ${device.capabilities.joinToString(", ")}",
            color = KerenGrey,
            fontSize = 12.sp
        )
        if (device.currentTask != null) {
            Text(text = "CURRENT: ${device.currentTask}", color = KerenAmber, fontSize = 12.sp)
        }
    }
}
