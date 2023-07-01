package com.soheibbettahar.litarus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = alabaster,
    primaryVariant = Color.White,
    secondary = lightCraminePink,
    background = blackChocolate,
    surface = Color.Black,
    onSurface = pearl,
    onBackground = alabaster,
    onPrimary = blackChocolate,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColors(
    primary = blackChocolate,
    primaryVariant = Color.Black,
    secondary = lightCraminePink,
    background = alabaster,
    surface = pearl,
    onSurface = blackChocolate,
    onBackground = blackChocolate,
    onPrimary = alabaster,
    onSecondary = Color.White
)

@Composable
fun LitarusTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}