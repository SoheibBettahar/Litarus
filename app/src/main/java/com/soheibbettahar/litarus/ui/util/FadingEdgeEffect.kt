package com.soheibbettahar.litarus.ui.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*

fun Modifier.withFadingEdgeEffect(): Modifier =
    graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithCache {

            val colors = mutableListOf(Color.Transparent)
                .apply { repeat(50) { add(Color.Black) } }
                .apply { add(Color.Transparent) }
            val gradientBrush = Brush.verticalGradient(colors.toList())

            onDrawWithContent {
                drawContent()
                drawRect(
                    brush = gradientBrush,
                    blendMode = BlendMode.DstIn
                )
            }

        }