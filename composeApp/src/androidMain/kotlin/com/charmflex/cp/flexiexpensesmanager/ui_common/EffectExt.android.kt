package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.skydoves.cloudy.cloudy

@Composable
actual fun BlurredBackgroundBox(
    blur: Boolean,
    content: @Composable () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .cloudy(enabled = blur)

    ) {
        content()
        if (blur) {
            val interactionSource = remember { MutableInteractionSource() }
            Box(modifier = Modifier.fillMaxSize().clickable(indication = null, interactionSource = interactionSource, onClick = {})) {

            }
        }
    }


}