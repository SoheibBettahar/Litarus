package com.example.guttenburg.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*

fun Modifier.withFadingEdgeEffect(): Modifier =
    graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            val colors = mutableListOf(Color.Transparent)
                .apply { repeat(50) { add(Color.Black) } }
                .apply { add(Color.Transparent) }
            drawContent()
            drawRect(
                brush = Brush.verticalGradient(colors.toList()),
                blendMode = BlendMode.DstIn
            )
        }