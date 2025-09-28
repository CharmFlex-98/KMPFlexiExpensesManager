package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

@Composable
fun Modifier.shimmerEffect(): Modifier {
    return composed {
        var size by remember {mutableStateOf(IntSize.Zero) }
        val transition = rememberInfiniteTransition()
        val startOffsetX by transition.animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(1000)
            )
        )

        return@composed background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xEBDDDDDD),
                    Color(0xEBAFAFAF),
                    Color(0xEBDDDDDD)
                ),
                start = Offset(startOffsetX, 0f),
                end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
            )
        ).onGloballyPositioned {
            size = it.size
        }
    }
}

@Composable
expect fun BlurredBackgroundBox(
    blur: Boolean = false,
    content: @Composable () -> Unit
)