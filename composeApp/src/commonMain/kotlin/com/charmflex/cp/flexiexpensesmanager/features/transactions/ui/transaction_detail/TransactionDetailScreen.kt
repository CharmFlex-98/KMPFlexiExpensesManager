package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLabel
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val detail = viewState.detail

    SGScaffold(
        topBar = {
            TransactionDetailTopBar(
                onDelete = viewModel::openDeleteWarningDialog,
                onEdit = viewModel::navigateToTransactionEdit,
                allowEdit = detail?.allowEdit ?: false
            )
        },
        modifier = Modifier.padding(horizontal = grid_x2),
        screenName = "TransactionDetailScreen"
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

@Composable
internal fun MainDetailCard(detail: TransactionDetailViewState.Detail) {
    DetailCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(grid_x3)
        ) {
            // Header Section with name and type badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FEHeading4(
                    text = detail.transactionName,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                
                TransactionTypeBadge(transactionType = detail.transactionTypeCode)
            }
        }
    }
}

@Composable
internal fun BankDetailCard(detail: TransactionDetailViewState.Detail) {
    DetailCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(grid_x3)
        ) {
            Row {
                FEHeading4(
                    text = stringResource(Res.string.generic_account_detail),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            val fromAccount = when (detail.transactionTypeCode) {
                TransactionType.EXPENSES.toString(), TransactionType.TRANSFER.toString(), TransactionType.UPDATE_ACCOUNT.toString() -> detail.transactionAccountFrom
                else -> null
            }

            val toAccount = when (detail.transactionTypeCode) {
                TransactionType.EXPENSES.toString()-> null
                else -> detail.transactionAccountTo
            }

            val fromAmount = when (detail.transactionTypeCode) {
                TransactionType.EXPENSES.toString() -> detail.formattedAccountTransactionAmount
                TransactionType.TRANSFER.toString() -> detail.formattedTransactionAmount
                TransactionType.UPDATE_ACCOUNT.toString() -> {
                    detail.transactionAccountFrom?.let {
                        detail.formattedTransactionAmount
                    }
                }
                else -> null
            }

            val toAmount = when (detail.transactionTypeCode) {
                TransactionType.INCOME.toString() -> detail.formattedTransactionAmount
                TransactionType.TRANSFER.toString() -> detail.formattedAccountTransactionAmount
                TransactionType.UPDATE_ACCOUNT.toString() -> {
                    detail.transactionAccountTo?.let {
                        detail.formattedAccountTransactionAmount
                    }
                }
                else -> null
            }

            fromAccount?.let {
                DetailRow(
                    label = stringResource(Res.string.generic_withdraw_from),
                    value = it.accountName,
                    iconRes = Res.drawable.bank,
                    suffixIconRes = if (it.isDeleted) Res.drawable.delete_alert_outline else null,
                    suffixIconColor = Color.Red
                )
                DetailRow(
                    label = stringResource(Res.string.generic_currency),
                    value = it.currency,
                    iconRes = Res.drawable.currency_sign
                )
            }

            fromAmount?.let {
                DetailRow(stringResource(Res.string.generic_account), value = it, Res.drawable.tag_multiple_outline)
            }

            if (toAccount == null && toAmount == null) return@DetailCard

            if (fromAccount != null || fromAmount != null) {
                Divider(modifier = Modifier.fillMaxSize())
            }

            toAccount?.let {
                DetailRow(
                    label = stringResource(Res.string.generic_bank_in),
                    value = it.accountName,
                    iconRes = Res.drawable.bank,
                    suffixIconRes = if (it.isDeleted) Res.drawable.delete_alert_outline else null,
                    suffixIconColor = Color.Red
                )
                DetailRow(
                    label = stringResource(Res.string.generic_currency),
                    value = it.currency,
                    iconRes = Res.drawable.currency_sign
                )
            }

            toAmount?.let {
                DetailRow(stringResource(Res.string.generic_amount), value = it, Res.drawable.tag_multiple_outline)
            }
        }
    }
}

@Composable
internal fun AdditionalDetailCard(detail: TransactionDetailViewState.Detail) {
    DetailCard {
        Column(
            verticalArrangement = Arrangement.spacedBy(grid_x3)
        ) {
            // Header Section with name and type badge
            FEHeading4(
                text = stringResource(Res.string.generic_additional_detail),
                color = MaterialTheme.colorScheme.onSurface,
            )

            DetailRow(
                label = stringResource(Res.string.generic_transaction_type),
                value = getTransactionTypeDisplayName(detail.transactionTypeCode),
                iconRes = Res.drawable.ic_transfer_icon
            )

            DetailRow(
                label = stringResource(Res.string.generic_date),
                value = detail.transactionDate,
                iconRes = Res.drawable.ic_calendar
            )

            detail.transactionCategory?.let { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(grid_x1_5)
                ) {
                    Box(
                        modifier = Modifier
                            .size(grid_x4)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(grid_x0_5)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.tag_multiple_outline),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(grid_x2)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        FEMetaData1(
                            text = stringResource(Res.string.generic_category),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        FEBody2(
                            text = category.name,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}


@Composable
internal fun DetailCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.6f)
            ),
        shape = RoundedCornerShape(grid_x2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2)
        ) {
            content()
        }
    }
}

@Composable
internal fun DetailRow(
    label: String,
    value: String,
    iconRes: DrawableResource,
    suffixIconRes: DrawableResource? = null,
    suffixIconColor: Color = LocalContentColor.current,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(grid_x1_5)
    ) {
        Box(
            modifier = Modifier
                .size(grid_x4)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(grid_x0_5)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(grid_x2)
            )
        }
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            FEMetaData1(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FEBody2(
                text = value,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        suffixIconRes?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = "",
                modifier = Modifier.size(grid_x3),
                tint = suffixIconColor
            )
        }
    }
}


@Composable
internal fun TransactionTypeBadge(transactionType: String) {
    val (backgroundColor, textColor, text) = when (transactionType) {
        TransactionType.INCOME.toString() -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.1f),
            Color(0xFF4CAF50),
            transactionType
        )
        TransactionType.EXPENSES.toString() -> Triple(
            Color(0xFFF44336).copy(alpha = 0.1f),
            Color(0xFFF44336),
            transactionType
        )
        TransactionType.TRANSFER.toString(), TransactionType.UPDATE_ACCOUNT.toString() -> Triple(
            Color(0xFF2196F3).copy(alpha = 0.1f),
            Color(0xFF2196F3),
            transactionType
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            transactionType
        )
    }
    
    SGLabel(
        text = text,
        textColor = textColor,
        containerColor = backgroundColor,
        fontSize = 12.sp
    )
}

@Composable
internal fun getTransactionTypeDisplayName(transactionType: String): String {
    return when (transactionType) {
        TransactionType.INCOME.toString() -> stringResource(Res.string.generic_income)
        TransactionType.EXPENSES.toString() -> stringResource(Res.string.generic_expenses)
        TransactionType.UPDATE_ACCOUNT.toString() -> stringResource(Res.string.generic_account_transfer)
        else -> transactionType
    }
}

@Composable
internal fun TransactionDetailTopBar(
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    allowEdit: Boolean
) {
    BasicTopBar(
        stringResource(Res.string.transaction_detail_screen_title),
        actions = {
            Row {
                if (allowEdit) IconButton(onClick = onEdit) {
                    SGIcons.EditIcon()
                }
                IconButton(onClick = onDelete) {
                    SGIcons.Delete()
                }
            }
        }
    )
}
