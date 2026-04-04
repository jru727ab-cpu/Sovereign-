package com.sovereign.civiltas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SovereignDarkColorScheme = darkColorScheme(
    primary = SovereignGold,
    onPrimary = SovereignCharcoal,
    primaryContainer = SovereignSurfaceVariant,
    onPrimaryContainer = SovereignGoldLight,
    secondary = SovereignSilver,
    onSecondary = SovereignCharcoal,
    tertiary = SovereignEmber,
    onTertiary = SovereignOnSurface,
    error = SovereignEmberLight,
    errorContainer = Color(0xFF3B0000),
    onErrorContainer = SovereignEmberLight,
    background = SovereignCharcoal,
    onBackground = SovereignOnSurface,
    surface = SovereignSurface,
    onSurface = SovereignOnSurface,
    surfaceVariant = SovereignSurfaceVariant,
    onSurfaceVariant = SovereignOnSurfaceVariant,
)

@Composable
fun SovereignTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SovereignDarkColorScheme,
        typography = SovereignTypography,
        content = content,
    )
}
