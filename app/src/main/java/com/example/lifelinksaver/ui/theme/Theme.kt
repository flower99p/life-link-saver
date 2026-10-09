package com.example.lifelinksaver.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFB8C9A9),
    secondary = Color(0xFFDDE9D5),
    background = Color(0xFFF3F0EC),
    surface = Color(0xFFF4F4F2),
    onPrimary = Color(0xFF1F1F1F),
    onBackground = Color(0xFF1F1F1F),
    onSurface = Color(0xFF1F1F1F),
)

@Composable
fun LifeLinkSaverTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}