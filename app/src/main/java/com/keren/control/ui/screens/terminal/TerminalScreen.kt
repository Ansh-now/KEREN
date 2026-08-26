package com.keren.control.ui.screens.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keren.control.domain.model.ConnectionState
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.TerminalViewModel
import kotlinx.coroutines.launch

@Composable
fun TerminalScreen(
    viewModel: TerminalViewModel = hiltViewModel()
) {
    var command by remember { mutableStateOf("") }
    val lines by viewModel.lines.collectAsState()
    val connection by viewModel.connection.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(lines.size) {
        if (lines.isNotEmpty()) {
            listState.animateScrollToItem(lines.lastIndex)
        }
    }

    fun send() {
        val c = command
        command = ""
        viewModel.submit(c)
        scope.launch {
            // ensure scroll after submit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("TERMINAL", color = KerenBlue, fontSize = 18.sp)
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
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(KerenSurface, RoundedCornerShape(8.dp))
                .border(1.dp, KerenBorder, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            items(lines) { line ->
                Text(
                    text = line,
                    color = when {
                        line.startsWith("$") -> KerenGreen
                        line.startsWith("[stderr]") || line.contains("FAILED") || line.startsWith("ERROR") -> KerenRed
                        line.startsWith("[") -> KerenGrey
                        else -> KerenText
                    },
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { send() })
            )
        }
    }
}
