package com.civiltas.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Gold,
    secondary = SkyBlue,
    tertiary = Emerald,
    background = DeepNavy,
    surface = SlateSurface,
    surfaceVariant = SlateLight,
    onPrimary = DeepNavy,
    onSecondary = DeepNavy,
    onTertiary = DeepNavy,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextMuted,
    error = DangerRed
)

@Composable
fun CiviltasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
