package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes
import kotlinx.serialization.Serializable


@Serializable
internal object AuthRoutes {
    @Serializable
    object Root : NavigationRoute

    @Serializable
    object Landing : NavigationRoute
}
