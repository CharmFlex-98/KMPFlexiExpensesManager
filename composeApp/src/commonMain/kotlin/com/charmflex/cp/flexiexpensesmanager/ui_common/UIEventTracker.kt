package com.charmflex.cp.flexiexpensesmanager.ui_common

import kotlinx.coroutines.CoroutineScope

expect suspend fun CoroutineScope.uiEventTrack(screenName: String)