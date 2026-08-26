package com.keren.control.ui.screens.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

@Composable
fun OverviewScreen() {
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

        // System Status
        StatusCard(
            title = "SYSTEM STATUS",
            items = listOf(
                "Core" to "ONLINE",
                "Devices" to "2 ONLINE",
                "Active Tasks" to "0",
                "Queued" to "0",
                "Completed" to "12"
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
            items(
                listOf(
                    "[14:28:04] Task received",
                    "[14:28:04] Task classified",
                    "[14:28:05] Routed to PC-NODE-01",
                    "[14:28:05] Command executing",
                    "[14:28:07] Process completed",
                    "[14:28:07] Result received"
                )
            ) { line ->
                Text(
                    text = line,
                    color = KerenText,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
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
                Text(
                    text = value,
                    color = if (value.contains("ONLINE")) KerenGreen else KerenText,
                    fontSize = 13.sp
                )
            }
        }
    }
}
