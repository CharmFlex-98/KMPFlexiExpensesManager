package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.icon_people
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SGRoundImage(
    modifier: Modifier = Modifier,
    placeHolderIconId: DrawableResource? = null,
    errorIconId: DrawableResource? = null,
    fallbackIconId: DrawableResource = Res.drawable.icon_people,
    source: Any?,
) {
    SGImage(modifier = modifier, placeHolderIconId = placeHolderIconId, errorIconId = errorIconId, fallbackIconId = fallbackIconId, source = source, shape = CircleShape)
}

@Composable
fun SGRoundCornerImage(
    modifier: Modifier = Modifier,
    placeHolderIconId: DrawableResource? = null,
    errorIconId: DrawableResource? = null,
    fallbackIconId: DrawableResource = Res.drawable.icon_people,
    source: Any?,
) {
    SGImage(modifier = modifier, source = source, placeHolderIconId = placeHolderIconId, errorIconId = errorIconId, fallbackIconId = fallbackIconId, shape = RoundedCornerShape(
        grid_x2
    ))
}

@Composable
fun SGImage(
    modifier: Modifier = Modifier,
    source: Any?,
    placeHolderIconId: DrawableResource? = null,
    errorIconId: DrawableResource? = null,
    fallbackIconId: DrawableResource = Res.drawable.icon_people, // change later
    shape: Shape? = null
) {
    val context = LocalPlatformContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(source)
            .build(),
        placeholder = placeHolderIconId?.let { painterResource(placeHolderIconId) },
        fallback = painterResource(fallbackIconId),
        error = errorIconId?.let { painterResource(errorIconId) },
        contentScale = ContentScale.Crop,
        modifier = modifier
            .run {
                if (shape != null) {
                    clip(shape)
                } else this
            },
        contentDescription = null
    )
}

@Composable
fun ImageOverlay(
    modifier: Modifier = Modifier,
    colorTop: Color = Color(0F, 0F, 0F, 0F),
    colorBottom: Color = Color(0F, 0F, 0F, 0.75F),
    alpha: Float = 0.5f,
    shape: Shape
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(colorTop, colorBottom)
                ),
                shape = shape
            )
            .alpha(alpha)
    ) {}
}

@Composable
fun InvalidOverlay(
    modifier: Modifier = Modifier,
    shape: Shape
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0F, 0F, 0F, 0.45F),
                shape = shape
            )
    ) {}
}

@Composable
@Preview
private fun RoundImagePreview() {
    SGRoundImage(source = "source/sdk/ui-common/src/main/assets/sample.jpeg")
}