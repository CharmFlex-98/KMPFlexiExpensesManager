package com.charmflex.cp.flexiexpensesmanager.features.account.destinations

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            val importFixAccountName = it.arguments?.getString(AccountRoutes.Args.IMPORT_FIX_ACCOUNT_NAME)
            val viewModel = getViewModel {
                appComponent.accountEditorViewModel.apply {
                    initFlow(importFixAccountName)
                }
            }
            AccountEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.importAccountEditor() {
        composable<AccountRoutes.ImportEditorAccountRoute>{
            val importFixAccountName = it.arguments?.getString(AccountRoutes.Args.IMPORT_FIX_ACCOUNT_NAME)
            val viewModel = getViewModel {
                appComponent.accountEditorViewModel.apply {
                    initFlow(importFixAccountName)
                }
            }
            AccountEditorScreen(viewModel = viewModel)
        }
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
                appComponent.accountDetailViewModelFactory.create(accountId = accountId, filterFromPreviousScreen)
            }
            AccountDetailScreen(accountDetailViewModel)
        }
    }
}