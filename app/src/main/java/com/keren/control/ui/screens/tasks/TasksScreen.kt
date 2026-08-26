package com.keren.control.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

data class TaskUi(
    val id: String,
    val command: String,
    val target: String,
    val status: String
)

@Composable
fun TasksScreen() {
    val queued = listOf(
        TaskUi("#104", "python --version", "PC-NODE-01", "QUEUED"),
        TaskUi("#105", "ls -la", "PC-NODE-01", "QUEUED")
    )
    val executed = listOf(
        TaskUi("#101", "npm install", "PC-NODE-01", "COMPLETED"),
        TaskUi("#102", "git pull", "PC-NODE-01", "COMPLETED"),
        TaskUi("#103", "docker compose up", "PC-NODE-01", "FAILED")
    )

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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(queued) { TaskRow(it) }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("EXECUTED", color = KerenGreen, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(executed) { TaskRow(it) }
        }
    }
}

@Composable
private fun TaskRow(task: TaskUi) {
    val statusColor = when (task.status) {
        "COMPLETED" -> KerenGreen
        "FAILED" -> KerenRed
        "QUEUED" -> KerenAmber
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
        Column {
            Text("${task.id}  ${task.command}", color = KerenText, fontSize = 13.sp)
            Text(task.target, color = KerenGrey, fontSize = 11.sp)
        }
        Text(task.status, color = statusColor, fontSize = 12.sp)
    }
}
