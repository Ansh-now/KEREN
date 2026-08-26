package com.keren.control.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keren.control.domain.model.ConnectionState
import com.keren.control.ui.theme.*
import com.keren.control.ui.viewmodel.ConnectionViewModel

@Composable
fun SettingsScreen(
    viewModel: ConnectionViewModel = hiltViewModel()
) {
    val connection by viewModel.connection.collectAsState()
    val config by viewModel.config.collectAsState()

    var httpUrl by remember(config.httpBaseUrl) { mutableStateOf(config.httpBaseUrl) }
    var wsUrl by remember(config.wsUrl) { mutableStateOf(config.wsUrl) }
    var token by remember(config.authToken) { mutableStateOf(config.authToken.orEmpty()) }

    val statusColor = when (connection.state) {
        ConnectionState.CONNECTED -> KerenGreen
        ConnectionState.CONNECTING, ConnectionState.RECONNECTING -> KerenAmber
        ConnectionState.ERROR -> KerenRed
        ConnectionState.DISCONNECTED -> KerenGrey
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("SETTINGS", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Text("CONNECTION", color = KerenGrey, fontSize = 11.sp)
        Text(
            text = connection.state.name,
            color = statusColor,
            fontSize = 14.sp
        )
        connection.lastError?.let {
            Text(text = it, color = KerenRed, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("HTTP Base URL", color = KerenGrey, fontSize = 12.sp)
        ConfigField(value = httpUrl, onValueChange = { httpUrl = it }, hint = "http://192.168.1.10:8080")

        Spacer(modifier = Modifier.height(12.dp))
        Text("WebSocket URL (optional)", color = KerenGrey, fontSize = 12.sp)
        ConfigField(value = wsUrl, onValueChange = { wsUrl = it }, hint = "ws://192.168.1.10:8080/v0.6/ws")

        Spacer(modifier = Modifier.height(12.dp))
        Text("Auth Token (optional)", color = KerenGrey, fontSize = 12.sp)
        ConfigField(value = token, onValueChange = { token = it }, hint = "Bearer token")

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    viewModel.updateConfig(httpUrl, wsUrl, token)
                    viewModel.connect()
                },
                colors = ButtonDefaults.buttonColors(containerColor = KerenBlue, contentColor = KerenBlack)
            ) {
                Text("CONNECT")
            }
            Button(
                onClick = { viewModel.disconnect() },
                colors = ButtonDefaults.buttonColors(containerColor = KerenSurfaceVariant, contentColor = KerenText)
            ) {
                Text("DISCONNECT")
            }
            Button(
                onClick = { viewModel.resync() },
                colors = ButtonDefaults.buttonColors(containerColor = KerenSurfaceVariant, contentColor = KerenText)
            ) {
                Text("RESYNC")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Protocol Version", color = KerenGrey, fontSize = 12.sp)
        Text("0.6", color = KerenText, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("App Version", color = KerenGrey, fontSize = 12.sp)
        Text("0.6.0", color = KerenText, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Note: Emulator → use 10.0.2.2 for host PC. Real phone → use PC LAN IP.",
            color = KerenTextDim,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ConfigField(value: String, onValueChange: (String) -> Unit, hint: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(KerenSurface, RoundedCornerShape(6.dp))
            .border(1.dp, KerenBorder, RoundedCornerShape(6.dp))
            .padding(12.dp)
    ) {
        if (value.isEmpty()) {
            Text(hint, color = KerenTextDim, fontSize = 13.sp)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = androidx.compose.ui.text.TextStyle(
                color = KerenText,
                fontSize = 13.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            ),
            cursorBrush = SolidColor(KerenBlue),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
