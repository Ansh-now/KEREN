package com.keren.control.ui.screens.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.LogsViewModel

@Composable
fun LogsScreen(
    viewModel: LogsViewModel = hiltViewModel()
) {
    val events by viewModel.recentEvents.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("LOGS", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            Text(
                text = "No events yet. Connect to Core and run tasks.",
                color = KerenTextDim,
                fontSize = 12.sp
            )
        } else {
            LazyColumn {
                items(events, key = { it.eventId }) { event ->
                    val line = buildString {
                        append("[")
                        append(event.timestamp)
                        append("] ")
                        append(event.event)
                        event.taskId?.let { append(" task="); append(it) }
                        event.deviceId?.let { append(" device="); append(it) }
                        event.targetNodeId?.let { append(" → "); append(it) }
                    }
                    Text(
                        text = line,
                        color = when {
                            event.event.contains("failed") || event.event.contains("error") -> KerenRed
                            event.event.contains("completed") -> KerenGreen
                            else -> KerenText
                        },
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }
            }
        }
    }
}
