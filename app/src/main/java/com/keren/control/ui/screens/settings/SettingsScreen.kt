package com.keren.control.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp)
    ) {
        Text("SETTINGS", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Text("KEREN Core Address", color = KerenGrey, fontSize = 12.sp)
        Text("ws://192.168.1.10:8080", color = KerenText, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Protocol Version", color = KerenGrey, fontSize = 12.sp)
        Text("0.6", color = KerenText, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))
        Text("App Version", color = KerenGrey, fontSize = 12.sp)
        Text("0.6.0", color = KerenText, fontSize = 14.sp)
    }
}
