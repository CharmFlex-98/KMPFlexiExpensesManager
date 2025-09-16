package com.charmflex.cp.flexiexpensesmanager.features.billing.destination

import BillingRoutes
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.billing.ui.BillingScreen
import com.charmflex.cp.flexiexpensesmanager.features.billing.ui.BillingScreenWithOptions

class BillingDestinationBuilder : DestinationBuilder {
    private val appComponent = AppComponentProvider.instance.getAppComponent()

    override fun NavGraphBuilder.buildGraph() {
        billing()
    }

    private fun NavGraphBuilder.billing() {
        composable<BillingRoutes.Root> {
            val viewModel = getViewModel { appComponent.billingViewModel }
            LaunchedEffect(Unit) {
                viewModel.init()
            }
            BillingScreen(viewModel)
        }
    }
}