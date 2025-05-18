package com.charmflex.cp.flexiexpensesmanager.features.session.di

import com.charmflex.flexiexpensesmanager.features.session.SessionManager

internal interface SessionInjector {
    val sessionManager: SessionManager
}