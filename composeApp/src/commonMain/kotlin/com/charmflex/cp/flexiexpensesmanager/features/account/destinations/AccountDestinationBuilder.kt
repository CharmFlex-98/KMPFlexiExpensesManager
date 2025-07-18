package com.charmflex.cp.flexiexpensesmanager.features.account.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEVerticalSlideUp
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getInt
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getString
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AccountRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilterNavType
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.AccountEditorScreen
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.account_detail.AccountDetailScreen
import kotlin.reflect.typeOf

internal class AccountDestinationBuilder(
    private val navController: NavController
) : DestinationBuilder {
    private val appComponent = AppComponentProvider.instance.getAppComponent()
    override fun NavGraphBuilder.buildGraph() {
        accountEditor()
        importAccountEditor()
        accountDetailScreen()
    }

    private fun NavGraphBuilder.accountEditor() {
        composable<AccountRoutes.EditorAccountRouteDefault>{
            val route = it.toRoute<AccountRoutes.EditorAccountRouteDefault>()
            getAccountEditorScreen(route)
        }
    }

    private fun NavGraphBuilder.importAccountEditor() {
        composable<AccountRoutes.ImportEditorAccountRoute>{
            val route = it.toRoute<AccountRoutes.ImportEditorAccountRoute>()
            getAccountEditorScreen(route)
        }
    }

    @Composable
    private fun getAccountEditorScreen(route: AccountRoutes.EditorAccountRoute) {
        var importFixAccountName: String? = null
        when (route) {
            is AccountRoutes.ImportEditorAccountRoute -> {
                importFixAccountName = route.fixAccountName
            }
            else -> {}
        }
        val viewModel = getViewModel {
            appComponent.accountEditorViewModel().apply {
                initFlow(importFixAccountName)
            }
        }
        AccountEditorScreen(viewModel = viewModel)
    }


    private fun NavGraphBuilder.accountDetailScreen() {
        composable<AccountRoutes.DetailAccountRoute>(
            enterTransition = FEVerticalSlideUp,
            typeMap = mapOf(
                typeOf<DateFilter?>() to DateFilterNavType
            )
        ) {
            val accountId = it.arguments?.getInt(AccountRoutes.Args.ACCOUNT_ID) ?: -1
            val filterFromPreviousScreen = remember {
                navController.previousBackStackEntry?.savedStateHandle?.remove<DateFilter>(
                    AccountRoutes.Args.ACCOUNT_DETAIL_DATE_FILTER)
            }
            val accountDetailViewModel = getViewModel {
                appComponent.accountDetailViewModelFactory().create(accountId = accountId, filterFromPreviousScreen)
            }
            AccountDetailScreen(accountDetailViewModel)
        }
    }
}