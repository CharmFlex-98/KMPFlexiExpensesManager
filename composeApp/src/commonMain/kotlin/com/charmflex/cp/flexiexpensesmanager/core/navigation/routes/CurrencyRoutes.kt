package com.charmflex.flexiexpensesmanager.core.navigation.routes

import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.NavigationRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.buildDestination
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.buildRoute
import kotlinx.serialization.Serializable

object CurrencyRoutes {
    object Args {
        const val CURRENCY_TYPE = "CURRENCY_TYPE"
        const val CURRENCY_TYPE_MAIN = "CURRENCY_TYPE_MAIN"
        const val CURRENCY_TYPE_SECONDARY = "CURRENCY_TYPE_SECONDARY"
        const val CURRENCY_TYPE_SECONDARY_EDIT = "CURRENCY_TYPE_SECONDARY_EDIT"
    }

    @Serializable
    data class CurrencySetting(
        val flowType: String
    ) : NavigationRoute

    @Serializable
    object UserSecondaryCurrency : NavigationRoute

    internal fun currencySettingDestination(flowType: String): NavigationRoute {
        return CurrencySetting(flowType)
    }
}