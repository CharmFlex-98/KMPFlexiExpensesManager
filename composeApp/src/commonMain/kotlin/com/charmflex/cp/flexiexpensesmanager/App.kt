package com.charmflex.cp.flexiexpensesmanager

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigatorListener
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AuthRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastState
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastType
import com.charmflex.cp.flexiexpensesmanager.features.account.destinations.AccountDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.announcement.destination.AnnouncementDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.auth.destination.AuthDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.backup.destination.BackupDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.billing.destination.BillingDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.budget.destinations.BudgetDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.category.category.destinations.CategoryDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.currency.destinations.CurrencyDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.home.destination.HomeDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.destinations.SchedulerDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.tag.destination.TagDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.transactions.destination.TransactionDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.theme.FlexiExpensesManagerTheme
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun App(
    routeNavigator: RouteNavigator,
    toastManager: ToastManager,
    onQuit: () -> Unit
) {
    val navController = rememberNavController()
    val state by toastManager.state.collectAsState()

    BackHandler {
        if (navController.popBackStack().not()) {
            onQuit()
        }
    }

    RouteNavigatorListener(routeNavigator = routeNavigator, navController = navController)

    FlexiExpensesManagerTheme {
        NavHost(navController = navController, startDestination = AuthRoutes.Root) {
            createDestinations(navController).forEach {
                with(it) { buildGraph() }
            }
        }

        SnackBarView(state) {
            toastManager.reset()
        }
    }
}

@Composable
internal fun SnackBarView(toastState: ToastState?, onReset: () -> Unit) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastState) {
        if (toastState != null) {
            snackBarHostState.showSnackBarImmediately(toastState.message)
            onReset()
        }
    }

    val snackBarType = when (toastState?.toastType) {
        ToastType.SUCCESS, ToastType.NEUTRAL -> SnackBarType.Success
        else -> SnackBarType.Error
    }
    SGSnackBar(snackBarHostState = snackBarHostState, snackBarType = snackBarType)
}

private fun createDestinations(navController: NavController): List<DestinationBuilder> {
    return listOf(
        AuthDestinationBuilder(),
        HomeDestinationBuilder(),
        TransactionDestinationBuilder(),
        CategoryDestinationBuilder(),
        AccountDestinationBuilder(navController),
        CurrencyDestinationBuilder(),
        TagDestinationBuilder(),
        BackupDestinationBuilder(),
        SchedulerDestinationBuilder(),
        BudgetDestinationBuilder(),
        BillingDestinationBuilder(),
        AnnouncementDestinationBuilder()
    )
}