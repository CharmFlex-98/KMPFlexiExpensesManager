package com.charmflex.cp.flexiexpensesmanager.features.session.di

import com.charmflex.cp.flexiexpensesmanager.features.session.SessionManager

internal interface SessionInjector {
    fun sessionManager(): SessionManager
}