package com.charmflex.cp.flexiexpensesmanager.features.scheduler.destinations

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.SchedulerRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList.SchedulerListScreen
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.SchedulerDetailScreen

internal class SchedulerDestinationBuilder : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }
    override fun NavGraphBuilder.buildGraph() {
        schedulerListScreen()
        schedulerDetailScreen()
    }

    private fun NavGraphBuilder.schedulerListScreen() {
        composable<SchedulerRoutes.SchedulerList> {
            val viewModel = getViewModel {
                appComponent.schedulerListViewModel()
            }

            LaunchedEffect(Unit) {
                viewModel.init()
            }

            SchedulerListScreen(transactionSchedulerListViewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.schedulerDetailScreen() {
        composable<SchedulerRoutes.SchedulerDetail> {
            val id = it.toRoute<SchedulerRoutes.SchedulerDetail>().id
            val viewModel = getViewModel {
                appComponent.schedulerDetailViewModelFactory().create(id)
            }

            SchedulerDetailScreen(viewModel)
        }
    }
}