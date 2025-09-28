package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
    }
}