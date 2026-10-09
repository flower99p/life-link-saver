package com.example.lifelinksaver.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Color(0xFF4C662B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDEDA3),
    onPrimaryContainer = Color(0xFF354E16),
    secondary = Color(0xFF586249),
    secondaryContainer = Color(0xFFDCE7C8),
    background = Color(0xFFF9FAEF),
    surface = Color(0xFFF9FAEF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB1D18A),
    onPrimary = Color(0xFF1F3701),
    primaryContainer = Color(0xFF354E16),
    onPrimaryContainer = Color(0xFFCDEDA3),
    secondary = Color(0xFFBFCBAD),
    secondaryContainer = Color(0xFF404A33),
    background = Color(0xFF11140E),
    surface = Color(0xFF11140E),
)

@Composable
fun LifeLinkSaverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(colorScheme = colors, content = content)
}
