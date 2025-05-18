package com.charmflex.cp.flexiexpensesmanager

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigatorListener
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.flexiexpensesmanager.core.navigation.routes.AuthRoutes
import com.charmflex.cp.flexiexpensesmanager.features.account.destinations.AccountDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.auth.destination.AuthDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.backup.destination.BackupDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.budget.destinations.BudgetDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.category.category.destinations.CategoryDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.currency.destinations.CurrencyDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.home.destination.HomeDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.destinations.SchedulerDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.tag.destination.TagDestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.features.transactions.destination.TransactionDestinationBuilder
import com.example.compose.FlexiExpensesManagerTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun App(
    routeNavigator: RouteNavigator,
    onQuit: () -> Unit
) {
    val navController = rememberNavController()

    BackHandler {
        if (navController.popBackStack().not()) {
            onQuit()
        }
    }

    RouteNavigatorListener(routeNavigator = routeNavigator, navController = navController)

    FlexiExpensesManagerTheme {
        NavHost(navController = navController, startDestination = AuthRoutes.ROOT) {
            createDestinations(navController).forEach {
                with(it) { buildGraph() }
            }
        }
    }
}

private fun createDestinations(navController: NavController): List<DestinationBuilder> {
    return listOf(
        AuthDestinationBuilder(),
        HomeDestinationBuilder(),
        TransactionDestinationBuilder(),
        CategoryDestinationBuilder(navController),
        AccountDestinationBuilder(navController),
        CurrencyDestinationBuilder(),
        TagDestinationBuilder(),
        BackupDestinationBuilder(),
        SchedulerDestinationBuilder(),
        BudgetDestinationBuilder()
    )
}