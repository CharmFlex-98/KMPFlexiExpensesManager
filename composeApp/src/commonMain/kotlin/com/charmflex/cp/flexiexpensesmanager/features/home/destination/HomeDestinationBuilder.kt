package com.charmflex.cp.flexiexpensesmanager.features.home.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.HomeRoutes
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.HomeScreen

internal class HomeDestinationBuilder : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }
    override fun NavGraphBuilder.buildGraph() {
        navigation<HomeRoutes.ROOT>(
            startDestination = HomeRoutes.MainHomeRoute
        ) {
            homeScreen()
        }
    }

    private fun NavGraphBuilder.homeScreen() {
        composable<HomeRoutes.MainHomeRoute> {
            val refreshOnScreenShown = it.savedStateHandle.remove<Boolean>(HomeRoutes.Args.HOME_REFRESH) ?: false

            HomeScreen(
                appComponent = appComponent,
                refreshOnHomeShown = refreshOnScreenShown
            )
        }
    }
}