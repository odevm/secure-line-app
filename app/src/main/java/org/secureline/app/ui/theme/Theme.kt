package org.secureline.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DeepTeal = Color(0xFF0B1F2A)
private val SignalCyan = Color(0xFF2BB8A8)
private val SoftMist = Color(0xFFE8F1F5)
private val WarmInk = Color(0xFF14212B)

private val LightColors = lightColorScheme(
    primary = DeepTeal,
    onPrimary = SoftMist,
    secondary = SignalCyan,
    onSecondary = DeepTeal,
    background = SoftMist,
    onBackground = WarmInk,
    surface = SoftMist,
    onSurface = WarmInk,
)

private val DarkColors = darkColorScheme(
    primary = SignalCyan,
    onPrimary = DeepTeal,
    secondary = SoftMist,
    onSecondary = DeepTeal,
    background = DeepTeal,
    onBackground = SoftMist,
    surface = Color(0xFF122833),
    onSurface = SoftMist,
)

@Composable
fun SecureLineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
