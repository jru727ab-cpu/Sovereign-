package com.sovereign.civiltas.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OreGold,
    onPrimary = Color.Black,
    secondary = KnowledgeBlue,
    onSecondary = Color.White,
    background = CiviltasDark,
    surface = CiviltasSurface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = CatastropheRed,
)

@Composable
fun CiviltasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
