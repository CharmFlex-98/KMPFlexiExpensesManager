package com.charmflex.cp.flexiexpensesmanager.features.session.di.module

import com.charmflex.cp.flexiexpensesmanager.features.session.SessionManager
import org.koin.dsl.module

val sessionModule = module {
    factory { SessionManager() }
}