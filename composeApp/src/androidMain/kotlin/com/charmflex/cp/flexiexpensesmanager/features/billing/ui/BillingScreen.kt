package com.charmflex.cp.flexiexpensesmanager.features.billing.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil3.request.Disposable
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager

@Composable
fun BillingScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    if (activity == null) {
        // Error Screen
        return
    }


    val billingManager = remember {
        AndroidBillingManager(activity)
    }

    DisposableEffect(billingManager) {
        onDispose {
            billingManager.cleanup()
        }
    }

    // Use billingManager...
}