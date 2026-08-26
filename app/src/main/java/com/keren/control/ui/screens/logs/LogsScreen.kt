package com.keren.control.ui.screens.logs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

@Composable
fun LogsScreen() {
    val logs = listOf(
        "[14:32:04] [TASK] task_102 created",
        "[14:32:04] [ROUTER] selected PC-NODE-01",
        "[14:32:04] [NODE] task dispatched",
        "[14:32:05] [EXEC] process started",
        "[14:32:08] [EXEC] process completed",
        "[14:32:08] [TASK] task_102 completed"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("LOGS", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(logs) { line ->
                Text(
                    text = line,
                    color = KerenText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
        }
    }
}
