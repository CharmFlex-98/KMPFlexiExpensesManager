package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

internal object BudgetRoutes {
    @Serializable
    object BudgetSetting : NavigationRoute

    @Serializable
    object BudgetDetail : NavigationRoute
}