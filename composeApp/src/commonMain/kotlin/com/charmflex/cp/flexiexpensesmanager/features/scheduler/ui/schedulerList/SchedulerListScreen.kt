package com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.ExpensesHistoryItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.LockedFeatureButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultAnimation
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.error_image
import kotlinproject.composeapp.generated.resources.generic_error
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource


@Composable
internal fun SchedulerListScreen(transactionSchedulerListViewModel: SchedulerListViewModel) {
    val viewState by transactionSchedulerListViewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarState by transactionSchedulerListViewModel.snackBarState.collectAsState()
    val genericError = stringResource(Res.string.generic_error)

    LaunchedEffect(snackBarState) {
        when (val state = snackBarState) {
            is SnackBarState.Error -> {
                snackbarHostState.showSnackBarImmediately(state.message ?: genericError)
                transactionSchedulerListViewModel.resetSnackbarState()
            }

            else -> {}
        }
    }

    SGScaffold(
        modifier = Modifier.padding(grid_x2),
        isLoading = viewState.isLoading,
        topBar = {
            if (viewState.isFeatureAllowed) BasicTopBar(
                actions = {
                    IconButton(onClick = { transactionSchedulerListViewModel.addScheduler() }) {
                        SGIcons.Add()
                    }
                }
            )
        },
        screenName = "SchedulerListScreen"
    ) {

        if (!viewState.isFeatureAllowed) {
            NoResultContent(modifier = Modifier.weight(1f), "This feature is only for premium user. Let's purchase it!")
            LockedFeatureButton(modifier = Modifier.fillMaxWidth(), "🔒 Unlock Scheduler") {
                transactionSchedulerListViewModel.navigateToPurchaseScreen()
            }
            return@SGScaffold
        }


        if (viewState.schedulerItems.isEmpty()) {
            NoResultContent(Modifier.weight(1f), "No scheduler is set. Create one?")
        } else {
            viewState.schedulerItems.forEach {
                ScheduledTransactionItem(
                    id = it.id,
                    name = it.name,
                    amount = it.amount,
                    category = it.category,
                    type = it.type.name,
                    iconResId = it.iconResId,
                ) {
                    transactionSchedulerListViewModel.navigateToDetail(it)
                }
            }
        }
    }

    SGSnackBar(snackBarHostState = snackbarHostState, snackBarType = SnackBarType.Error)
}

@Composable
private fun ScheduledTransactionItem(
    id: Long,
    name: String,
    amount: String,
    category: String,
    type: String,
    iconResId: DrawableResource?,
    onClick: (Long) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpensesHistoryItem(
            Modifier
                .weight(1f)
                .padding(vertical = grid_x1),
            id,
            name,
            amount,
            category,
            type,
            iconResId = iconResId ?: Res.drawable.error_image,
            onClick = onClick
        )
    }
}