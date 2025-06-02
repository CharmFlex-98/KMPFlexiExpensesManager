package com.charmflex.cp.flexiexpensesmanager.features.auth.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.charmflex.cp.flexiexpensesmanager.di.AppComponent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AuthRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.features.auth.ui.landing.LandingScreen
import com.charmflex.cp.flexiexpensesmanager.features.auth.ui.landing.LandingScreenViewModel

internal class AuthDestinationBuilder : DestinationBuilder {
    private val appComponent: AppComponent by lazy { AppComponentProvider.instance.getAppComponent() }

    override fun NavGraphBuilder.buildGraph() {
        navigation<AuthRoutes.Root>(
            startDestination = AuthRoutes.Landing
        ) {
            landingDestination()
            // LOGIN
            // REGISTER
        }
    }

    private fun NavGraphBuilder.landingDestination() {
        composable<AuthRoutes.Landing> {
            val landingScreenViewModel: LandingScreenViewModel = getViewModel { appComponent.landingScreenViewModel }
            LandingScreen(landingScreenViewModel = landingScreenViewModel)
        }
    }
}