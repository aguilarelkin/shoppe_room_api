package com.androsh.shopee.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    tertiary = Color(0xFF3700B3),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    onSurface = Color(0xFF000000),
)

val DarkColor = darkColorScheme(
    primary = Color(0xFF757575),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF424242),
    onPrimaryContainer = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFFBDBDBD),

    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFFFFA500),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFAE1A2),
    onTertiaryContainer = Color(0xFF000000),

    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = Color(0xFFFFFFFF),

    surfaceTint = Color(0xFF757575),  // Gris oscuro para tintado

    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF121212),

    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFCF6679),
    onErrorContainer = Color(0xFFFFFFFF),

    outline = Color(0xFF999999),
    outlineVariant = Color(0xFF616161),

    scrim = Color(0x99000000)
)
