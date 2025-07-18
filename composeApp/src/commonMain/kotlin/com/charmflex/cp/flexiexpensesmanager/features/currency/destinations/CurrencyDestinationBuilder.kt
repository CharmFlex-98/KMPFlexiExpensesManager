package com.charmflex.cp.flexiexpensesmanager.features.currency.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromEnd
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getString
import com.charmflex.flexiexpensesmanager.core.navigation.routes.CurrencyRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.CurrencySettingScreen
import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.UserCurrencyScreen

internal class CurrencyDestinationBuilder : DestinationBuilder {
    private val appComponent = AppComponentProvider.instance.getAppComponent()
    override fun NavGraphBuilder.buildGraph() {
        userSetCurrencyList()
        currencySetting()
    }

    private fun NavGraphBuilder.userSetCurrencyList() {
        composable<CurrencyRoutes.UserSecondaryCurrency>(
            enterTransition = FEHorizontalEnterFromEnd
        ) {
            val viewModel = getViewModel {
                appComponent.userCurrencyViewModel()
            }

            UserCurrencyScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.currencySetting() {
        composable<CurrencyRoutes.CurrencySetting>(
            enterTransition = FEHorizontalEnterFromEnd
        ) {
            val type = it.toRoute<CurrencyRoutes.CurrencySetting>().flowType
            val viewModel = getViewModel {
                appComponent.currencySettingViewModel().apply {
                    this.initialise(type)
                }
            }

            CurrencySettingScreen(viewModel = viewModel)
        }
    }
}