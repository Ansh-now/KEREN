package com.keren.control.ui.screens.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

@Composable
fun TerminalScreen() {
    var command by remember { mutableStateOf("") }
    val output = remember {
        mutableStateListOf(
            "KEREN TERMINAL",
            "────────────────────────────────────────",
            "",
            "[PC-NODE-01]",
            "$ python --version",
            "Python 3.12.3",
            "",
            "$ "
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("TERMINAL", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))

        // Output area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(KerenSurface, RoundedCornerShape(8.dp))
                .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            items(output) { line ->
                Text(
                    text = line,
                    color = when {
                        line.startsWith("$") -> KerenGreen
                        line.startsWith("[") -> KerenGrey
                        else -> KerenText
                    },
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(KerenSurface, RoundedCornerShape(8.dp))
                .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$ ", color = KerenGreen, fontSize = 14.sp)
            BasicTextField(
                value = command,
                onValueChange = { command = it },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = KerenText,
                    fontSize = 14.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                ),
                cursorBrush = SolidColor(KerenBlue),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
    }
}
