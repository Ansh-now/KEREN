package com.keren.control.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KerenDarkColorScheme = darkColorScheme(
    primary = KerenBlue,
    onPrimary = Color.White,
    secondary = KerenGreen,
    onSecondary = Color.Black,
    background = KerenBlack,
    onBackground = KerenText,
    surface = KerenSurface,
    onSurface = KerenText,
    surfaceVariant = KerenSurfaceVariant,
    onSurfaceVariant = KerenGrey,
    error = KerenRed,
    onError = Color.White,
    outline = KerenBorder
)

@Composable
fun KerenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = KerenDarkColorScheme,
        typography = Typography,
        content = content
    )
}
