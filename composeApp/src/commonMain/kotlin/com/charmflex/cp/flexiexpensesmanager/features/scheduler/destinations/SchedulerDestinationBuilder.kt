package com.charmflex.cp.flexiexpensesmanager.features.scheduler.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.SchedulerRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList.SchedulerListScreen

internal class SchedulerDestinationBuilder : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }
    override fun NavGraphBuilder.buildGraph() {
        schedulerListScreen()
    }

    private fun NavGraphBuilder.schedulerListScreen() {
        composable<SchedulerRoutes.SchedulerList> {
            val viewModel = getViewModel {
                appComponent.schedulerListViewModel()
            }
            SchedulerListScreen(transactionSchedulerListViewModel = viewModel)
        }
    }
}