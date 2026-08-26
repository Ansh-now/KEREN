package com.keren.control.ui.screens.tasks

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
import com.keren.control.domain.model.Task
import com.keren.control.domain.model.TaskStatus
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel()
) {
    val queued by viewModel.queued.collectAsState()
    val executing by viewModel.executing.collectAsState()
    val history by viewModel.history.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("TASKS", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Text("QUEUED", color = KerenAmber, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        if (queued.isEmpty()) {
            Text("No queued tasks", color = KerenTextDim, fontSize = 12.sp)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 160.dp)
            ) {
                items(queued, key = { it.id }) { TaskRow(it) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("EXECUTING", color = KerenBlueGlow, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        if (executing.isEmpty()) {
            Text("No executing tasks", color = KerenTextDim, fontSize = 12.sp)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 120.dp)
            ) {
                items(executing, key = { it.id }) { TaskRow(it) }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("HISTORY", color = KerenGreen, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        if (history.isEmpty()) {
            Text("No completed tasks yet", color = KerenTextDim, fontSize = 12.sp)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(history, key = { it.id }) { TaskRow(it) }
            }
        }
    }
}

@Composable
private fun TaskRow(task: Task) {
    val statusColor = when (task.status) {
        TaskStatus.COMPLETED -> KerenGreen
        TaskStatus.FAILED, TaskStatus.TIMEOUT -> KerenRed
        TaskStatus.QUEUED, TaskStatus.RECEIVED, TaskStatus.PLANNING -> KerenAmber
        TaskStatus.EXECUTING, TaskStatus.DISPATCHED -> KerenBlueGlow
        else -> KerenGrey
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(KerenSurface, RoundedCornerShape(6.dp))
            .border(1.dp, KerenBorder, RoundedCornerShape(6.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("${task.id}  ${task.command}", color = KerenText, fontSize = 13.sp)
            Text(
                text = task.targetNodeId ?: "—",
                color = KerenGrey,
                fontSize = 11.sp
            )
        }
        Text(task.status.name, color = statusColor, fontSize = 12.sp)
    }
}
