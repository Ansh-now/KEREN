package com.keren.control.ui.screens.nervous

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keren.control.ui.theme.*

@Composable
fun NervousSystemScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(KerenBlack)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("NERVOUS SYSTEM", color = KerenBlue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))

        // Simple visual flow
        NodeBox("PHONE", KerenBlue)
        Arrow()
        NodeBox("KEREN CORE", KerenGreen)
        Arrow()
        NodeBox("ROUTER", KerenAmber)
        Arrow()
        NodeBox("PC-NODE-01", KerenBlue)
        Arrow()
        NodeBox("PROCESS", KerenGrey)
        Arrow()
        NodeBox("RESULT → CORE → PHONE", KerenGreen)

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Live mode • Real events only",
            color = KerenTextDim,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun NodeBox(label: String, accent: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .background(KerenSurface, RoundedCornerShape(8.dp))
            .border(1.dp, accent, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = KerenText, fontSize = 13.sp)
    }
}

@Composable
private fun Arrow() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(16.dp)
                .background(KerenBorder)
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(KerenBlue)
        )
    }
}
