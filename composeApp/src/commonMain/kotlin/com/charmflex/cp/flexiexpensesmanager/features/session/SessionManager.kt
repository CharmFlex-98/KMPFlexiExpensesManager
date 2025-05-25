package com.charmflex.cp.flexiexpensesmanager.features.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class SessionManager constructor(

) {
    private val _sessionState = MutableStateFlow<SessionState>(SessionState.None)
    val sessionState = _sessionState.asStateFlow()

    fun updateSessionState(state: SessionState) {
        _sessionState.tryEmit(state)
    }
}

internal interface SessionState {
    object None : SessionState
    object Start : SessionState
}