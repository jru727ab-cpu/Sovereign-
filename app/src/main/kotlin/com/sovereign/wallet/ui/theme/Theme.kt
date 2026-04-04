package com.sovereign.wallet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    secondary = Color(0xFFFACC15),
    tertiary = Color(0xFF10B981),
    background = Color(0xFF020617),
    surface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFF0F172A),
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0369A1),
    secondary = Color(0xFFCA8A04),
    tertiary = Color(0xFF059669)
)

@Composable
fun SovereignTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
