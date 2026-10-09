package com.example.lifelinksaver.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF9CB385),
    onPrimary = Color(0xFF1F261C),
    primaryContainer = Color(0xFFCFDCC2),
    onPrimaryContainer = Color(0xFF1D2B1A),
    secondary = Color(0xFFB9D4B1),
    onSecondary = Color(0xFF1C2619),
    secondaryContainer = Color(0xFFDFEAD9),
    background = Color(0xFFF3F0EC),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFF8F5F1),
    onSurface = Color(0xFF202020),
    surfaceVariant = Color(0xFFE7E3DF),
    onSurfaceVariant = Color(0xFF4D4D4D),
    outline = Color(0xFFE0DDD8),
    inverseSurface = Color(0xFF1F1F1F),
    inverseOnSurface = Color(0xFFFDFBF8),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB1CC9F),
    onPrimary = Color(0xFF0E140D),
    background = Color(0xFF141414),
    onBackground = Color(0xFFF2F2F2),
    surface = Color(0xFF1B1B1B),
    onSurface = Color(0xFFF3F3F3),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFE3E3E3),
    outline = Color(0xFF3D3D3D),
)

@Composable
fun LifeLinkSaverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
