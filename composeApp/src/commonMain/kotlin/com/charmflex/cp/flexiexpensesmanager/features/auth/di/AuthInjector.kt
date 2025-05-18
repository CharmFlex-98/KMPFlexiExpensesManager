package com.charmflex.cp.flexiexpensesmanager.features.auth.di

import com.charmflex.flexiexpensesmanager.features.auth.ui.landing.LandingScreenViewModel

internal interface AuthInjector {
    val landingScreenViewModel: LandingScreenViewModel
}