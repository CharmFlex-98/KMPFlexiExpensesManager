package com.charmflex.cp.flexiexpensesmanager.ui_common

import com.posthog.PostHog
import kotlinx.coroutines.CoroutineScope

actual suspend fun CoroutineScope.uiEventTrack(screenName: String) {
    PostHog.screen(screenName)
}