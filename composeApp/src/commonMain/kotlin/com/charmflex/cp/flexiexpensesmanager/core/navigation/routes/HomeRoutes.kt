package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable


object HomeRoutes {
    @Serializable
    internal object ROOT: NavigationRoute
    @Serializable
    internal object MainHomeRoute : NavigationRoute
    @Serializable
    internal object SummaryHomeRoute: NavigationRoute
    @Serializable
    internal object DetailHomeRoute : NavigationRoute
    @Serializable
    internal object AccountsHomeRoute: NavigationRoute
    @Serializable
    internal object SettingHomeRoute : NavigationRoute

    object Args {
        const val HOME_REFRESH = "HOME/refresh"
    }
}