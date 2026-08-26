package com.keren.control.ui.screens.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keren.control.domain.model.ConnectionState
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.OverviewViewModel

@Composable
fun OverviewScreen(
    viewModel: OverviewViewModel = hiltViewModel()
) {
    val connection by viewModel.connection.collectAsState()
    val devices by viewModel.devices.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val events by viewModel.recentEvents.collectAsState()

    // Recompute from latest snapshots
    val coreLabel = when (connection.state) {
        ConnectionState.CONNECTED -> "CONNECTED"
        ConnectionState.CONNECTING -> "CONNECTING"
        ConnectionState.RECONNECTING -> "RECONNECTING"
        ConnectionState.ERROR -> "ERROR"
        ConnectionState.DISCONNECTED -> "DISCONNECTED"
    }
    val online = devices.count { it.status.name == "ONLINE" }
    val offline = devices.count { it.status.name == "OFFLINE" || it.status.name == "STALE" }
    val executing = tasks.count { it.status.name == "EXECUTING" || it.status.name == "DISPATCHED" }
    val queued = tasks.count {
        it.status.name in listOf("QUEUED", "RECEIVED", "PLANNING", "ROUTING")
    }
    val completed = tasks.count { it.status.name == "COMPLETED" }
    val failed = tasks.count { it.status.name == "FAILED" || it.status.name == "TIMEOUT" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text(
            text = "KEREN CONTROL CENTER",
            color = KerenBlue,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        StatusCard(
            title = "SYSTEM STATUS",
            items = listOf(
                "Core" to coreLabel,
                "Devices" to "$online ONLINE / $offline OFF",
                "Executing" to "$executing",
                "Queued" to "$queued",
                "Completed" to "$completed",
                "Failed" to "$failed"
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "LIVE ACTIVITY",
            color = KerenGrey,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(KerenSurface, RoundedCornerShape(8.dp))
                .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            if (events.isEmpty()) {
                item {
                    Text(
                        text = if (connection.state == ConnectionState.CONNECTED)
                            "Waiting for events…"
                        else
                            "Not connected. Open Settings → CONNECT.",
                        color = KerenTextDim,
                        fontSize = 12.sp
                    )
                }
            } else {
                items(events.take(50)) { event ->
                    Text(
                        text = "[${event.timestamp}] ${event.event}" +
                            (event.taskId?.let { " $it" } ?: ""),
                        color = KerenText,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(title: String, items: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(KerenSurface, RoundedCornerShape(8.dp))
            .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = title, color = KerenBlue, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(12.dp))
        items.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, color = KerenGrey, fontSize = 13.sp)
                val color = when {
                    value.contains("CONNECTED") || value.contains("ONLINE") -> KerenGreen
                    value.contains("ERROR") || value.contains("FAILED") -> KerenRed
                    value.contains("CONNECT") || value.contains("RECONNECT") -> KerenAmber
                    else -> KerenText
                }
                Text(text = value, color = color, fontSize = 13.sp)
            }
        }
    }
}
