package com.civiltas.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── CIVILTAS colour palette ───────────────────────────────────────────────────
// Dark, atmospheric: deep navy backgrounds, gold Archivist accents, amber Order accents

val NavyDeep = Color(0xFF020617)
val NavyCard = Color(0xFF0F172A)
val NavySurface = Color(0xFF1E293B)
val NavyBorder = Color(0xFF334155)

val Gold = Color(0xFFFACC15)
val GoldDim = Color(0xFFCA8A04)
val Amber = Color(0xFFF59E0B)

val SlateBlue = Color(0xFF38BDF8)
val SlateBlueDim = Color(0xFF0284C7)

val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextDim = Color(0xFF475569)

val Locked = Color(0xFF334155)
val Success = Color(0xFF10B981)
val Danger = Color(0xFFEF4444)

private val CiviltasDarkColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = NavyDeep,
    primaryContainer = GoldDim,
    onPrimaryContainer = TextPrimary,
    secondary = SlateBlue,
    onSecondary = NavyDeep,
    secondaryContainer = SlateBlueDim,
    onSecondaryContainer = TextPrimary,
    tertiary = Amber,
    background = NavyDeep,
    onBackground = TextPrimary,
    surface = NavyCard,
    onSurface = TextPrimary,
    surfaceVariant = NavySurface,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    error = Danger,
    onError = TextPrimary
)

@Composable
fun CiviltasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CiviltasDarkColorScheme,
        content = content
    )
}
