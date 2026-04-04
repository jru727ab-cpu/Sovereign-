package com.civiltas.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CiviltasPrimary,
    onPrimary = CiviltasOnPrimary,
    primaryContainer = CiviltasPrimaryContainer,
    onPrimaryContainer = CiviltasOnPrimaryContainer,
    secondary = CiviltasSecondary,
    onSecondary = CiviltasOnSecondary,
    secondaryContainer = CiviltasSecondaryContainer,
    onSecondaryContainer = CiviltasOnSecondaryContainer,
    tertiary = CiviltasTertiary,
    background = CiviltasBackground,
    onBackground = CiviltasOnBackground,
    surface = CiviltasSurface,
    onSurface = CiviltasOnSurface,
    surfaceVariant = CiviltasSurfaceVariant,
    error = CiviltasError
)

private val LightColorScheme = lightColorScheme(
    primary = CiviltasPrimary,
    onPrimary = CiviltasOnPrimary,
    primaryContainer = CiviltasPrimaryContainer,
    onPrimaryContainer = CiviltasOnPrimaryContainer,
    secondary = CiviltasSecondary,
    onSecondary = CiviltasOnSecondary,
    secondaryContainer = CiviltasSecondaryContainer,
    onSecondaryContainer = CiviltasOnSecondaryContainer,
    tertiary = CiviltasTertiary
)

@Composable
fun CiviltasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
