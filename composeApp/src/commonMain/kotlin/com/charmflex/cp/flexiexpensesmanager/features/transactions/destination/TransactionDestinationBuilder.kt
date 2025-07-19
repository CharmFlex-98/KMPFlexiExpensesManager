package com.charmflex.cp.flexiexpensesmanager.features.transactions.destination

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromEnd
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getLong
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorScreen
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.TransactionDetailScreen

internal class TransactionDestinationBuilder : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }
    override fun NavGraphBuilder.buildGraph() {
        newTransactionScreen()
        transactionEditorScreen()
        newScheduledTransactionScreen()
        scheduledTransactionEditorScreen()
        transactionDetailScreen()
    }

    private fun NavGraphBuilder.newTransactionScreen() {
        composable<TransactionRoute.NewTransaction>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            val viewModel = getViewModel {
                appComponent.transactionEditorViewModelFactory().create(null)
            }
            TransactionEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.newScheduledTransactionScreen() {
        composable<TransactionRoute.ScheduledTransaction>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            val viewModel = getViewModel {
                appComponent.schedulerEditorViewModelFactory().create(null)
            }
            TransactionEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.transactionEditorScreen() {
        composable<TransactionRoute.TransactionEditor>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            val transactionId = it.toRoute<TransactionRoute.TransactionEditor>().transactionId
            val viewModel = getViewModel {
                appComponent.transactionEditorViewModelFactory().create(transactionId)
            }
            TransactionEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.scheduledTransactionEditorScreen() {
        composable<TransactionRoute.ScheduledTransactionEditor>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            val scheduledTransactionId = it.toRoute<TransactionRoute.ScheduledTransactionEditor>().scheduledTransactionId.toLong()
            val viewModel = getViewModel {
                appComponent.schedulerEditorViewModelFactory().create(scheduledTransactionId)
            }
            TransactionEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.transactionDetailScreen() {
        composable<TransactionRoute.Detail>(
            enterTransition = FEHorizontalEnterFromEnd,
        ) {
            val transactionId = it.toRoute<TransactionRoute.Detail>().id
            val viewModel = getViewModel {
                appComponent.transactionDetailViewModelFactory()
                    .create(transactionId = transactionId)
            }
            TransactionDetailScreen(viewModel = viewModel)
        }
    }
}