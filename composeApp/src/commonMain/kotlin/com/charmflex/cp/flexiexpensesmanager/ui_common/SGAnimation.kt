package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieClipSpec
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.LottieCompositionResult
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinproject.composeapp.generated.resources.Res

@Composable
internal fun SGLottieAnimation(
    modifier: Modifier,
    composition: LottieComposition? = null,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    reverseOnRepeat: Boolean = false,
    clipSpec: LottieClipSpec? = null,
    speed: Float = 1f,
    iterations: Int = 1
) {
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying,
        restartOnPlay,
        reverseOnRepeat,
        clipSpec,
        speed,
        iterations,
    )

    Image(
        modifier = modifier,
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        contentDescription = "Lottie animation"
    )
}

@Composable
fun AnnouncementAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("announcement.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x16),
        composition = composition,
        iterations = Compottie.IterateForever
    )
}

@Composable
fun NoResultAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("no_result.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x14),
        composition = composition,
        iterations = Compottie.IterateForever
    )
}

@Composable
fun NoResultContent(
    modifier: Modifier = Modifier,
    description: String,
    fontSize: TextUnit = 12.sp
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NoResultAnimation()
            Text(
                text = description,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                fontSize = fontSize,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LoadingAnimationSurface(
    modifier: Modifier = Modifier,
) {
    Surface(color = Color.Black.copy(alpha = 0.7f), modifier = Modifier.fillMaxSize()) {
        val composition by getComposition("loading_anim.json")
        SGLottieAnimation(
            modifier = modifier,
            composition = composition,
            iterations = Compottie.IterateForever
        )
    }
}

@Composable
fun SuccessGreenTickAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("success_tick.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x16),
        composition = composition,
        iterations = 1
    )
}

@Composable
fun WarningAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("warning_anim.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x30),
        composition = composition,
        iterations = 1
    )
}

@Composable
fun SetupAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("setup_anim.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x30),
        composition = composition,
        iterations = 1
    )
}


@Composable
fun Loader(
    modifier: Modifier = Modifier,
) {
    val composition by getComposition("loading_dot_anim.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x10),
        composition = composition,
        iterations = Compottie.IterateForever
    )
}

@Composable
fun Welcome(
    modifier: Modifier = Modifier,
) {
    val composition by getComposition("welcome_anim.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x30),
        composition = composition,
        iterations = 1
    )
}

// General Animation
@Composable
fun SGAnimatedTransition(
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    isVisible: Boolean = true,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}

@Composable
fun BoxScope.CircularProgressIndicatorFullScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(interactionSource = interactionSource, indication = null, onClick = {}),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MoneyOutInAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("money_out_in.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x10),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

@Composable
fun Money3DAnimation(
    modifier: Modifier = Modifier
) {
    val composition by getComposition("money_3d.json")
    SGLottieAnimation(
        modifier = modifier.size(grid_x10),
        composition = composition,
        iterations = Compottie.IterateForever
    )
}

@Composable
private fun getComposition(filename: String): LottieCompositionResult {
    return rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/lotties/$filename").decodeToString()
        )
    }
}