package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_detail.SchedulerDetailViewModel
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SchedulerDetailScreen(
    viewModel: SchedulerDetailViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val detail = viewState.detail

    SGScaffold(
        topBar = {
            TransactionDetailTopBar(
                onDelete = viewModel::openDeleteWarningDialog,
                onEdit = {},
                allowEdit = false
            )
        },
        modifier = Modifier.padding(horizontal = grid_x2),
        screenName = "SchedulerDetailScreen"
    ) {
        if (viewState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp
                )
            }
        } else if (detail != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = grid_x2),
                verticalArrangement = Arrangement.spacedBy(grid_x2)
            ) {
                // Single comprehensive transaction detail card
                MainDetailCard(detail = detail)
                BankDetailCard(detail = detail)
                AdditionalDetailCard(detail = detail)
            }
        }
    }

    viewState.dialogState?.let {
        BackHandler {

        }

        val title = when (it) {
            is TransactionDetailViewState.SuccessDialog -> it.title
            is TransactionDetailViewState.DeleteDialogState -> stringResource(Res.string.generic_warning)
        }
        val subtitle = when (it) {
            is TransactionDetailViewState.SuccessDialog -> it.subtitle
            is TransactionDetailViewState.DeleteDialogState -> stringResource(Res.string.generic_delete_warning_subtitle)
        }
        val positiveButtonText = when (it) {
            is TransactionDetailViewState.SuccessDialog -> stringResource(Res.string.generic_back_to_home)
            is TransactionDetailViewState.DeleteDialogState -> stringResource(Res.string.generic_yes)
        }
        val negativeButtonText = when (it) {
            is TransactionDetailViewState.SuccessDialog -> null
            is TransactionDetailViewState.DeleteDialogState -> stringResource(Res.string.generic_cancel)
        }
        SGActionDialog(
            title = title,
            text = subtitle,
            onDismissRequest = { viewModel.onCloseDialog() },
            primaryButtonText = positiveButtonText,
            secondaryButtonText = negativeButtonText
        ) {
            when (it) {
                is TransactionDetailViewState.SuccessDialog -> viewModel.onBack()
                is TransactionDetailViewState.DeleteDialogState -> viewModel.deleteTransaction()
            }
        }
    }
}
