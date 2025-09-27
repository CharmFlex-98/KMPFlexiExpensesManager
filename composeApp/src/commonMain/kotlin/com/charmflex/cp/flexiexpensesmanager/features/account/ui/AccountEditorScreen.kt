package com.charmflex.cp.flexiexpensesmanager.features.account.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyTextFieldOutputFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyVisualTransformation
import com.charmflex.cp.flexiexpensesmanager.features.account.domain.model.AccountGroup
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.BottomSheetItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.EditorCard
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGTextField
import com.charmflex.cp.flexiexpensesmanager.ui_common.SearchBottomSheet
import com.charmflex.cp.flexiexpensesmanager.ui_common.SearchItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.SelectionItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_25
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun AccountEditorScreen(
    viewModel: AccountEditorViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val title = when (val selectedAccountGroup = viewState.selectedAccountGroup) {
        null -> stringResource(Res.string.generic_account_group)
        else -> selectedAccountGroup.accountGroupName
    }
    val editorLabel = when (viewState.editorState) {
        is AccountEditorViewState.AccountEditorState -> stringResource(Res.string.generic_account_name)
        else -> ""
    }
    val isEditorOpened = viewState.editorState != null
    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarState by viewModel.snackBarState.collectAsState()
    val currency = (viewState.editorState as? AccountEditorViewState.AccountEditorState)?.currency
        ?: viewState.primaryCurrencyCode
    val visualTransformation =
        remember(currency) {
            currency.let {
                viewModel.getCurrencyVisualTransformer(currency)
            }
        }
    val bottomSheetState = viewState.bottomSheetState
    val modalBottomSheetState = rememberModalBottomSheetState()
    val genericError = stringResource(Res.string.generic_error)

    LaunchedEffect(key1 = snackBarState) {
        when (val state = snackBarState) {
            is SnackBarState.Success -> snackBarHostState.showSnackBarImmediately(state.message)
            is SnackBarState.Error -> snackBarHostState.showSnackBarImmediately(
                state.message ?: genericError
            )

            else -> {}
        }
        viewModel.resetSnackBarState()
    }

    BackHandler {
        viewModel.back()
    }

    SGScaffold(
        modifier = Modifier.fillMaxSize().padding(grid_x2),
        topBar = {
            BasicTopBar(title = title)
        },
        screenName = "AccountEditorScreen"
    ) {
        if (isEditorOpened) {
            EditorScreen(
                modifier = Modifier.padding(vertical = 16.dp),
                editorLabel = editorLabel,
                viewState = viewState,
                updateAccountName = viewModel::updateAccountName,
                updateAmount = viewModel::updateInitialAmount,
                onFieldTap = {
                    viewModel.onTapField(it)
                },
                visualTransformation = visualTransformation
            ) {
                viewModel.addNewItem()
            }
        } else {
            MainContentScreen(
                viewState = viewState,
                onSelectAccountGroup = viewModel::selectAccountGroup,
                onDeleteAccount = { accountId ->
                    viewModel.launchDeleteDialog(
                        accountId.toInt(),
                        AccountEditorViewState.Type.ACCOUNT
                    )
                },
                onAddNewAccount = { viewModel.toggleEditor(true) }
            )
        }
    }

    viewState.deleteDialogState?.let {
        SGActionDialog(
            title = stringResource(Res.string.dialog_delete_item_title),
            text = stringResource(Res.string.dialog_delete_item_subtitle),
            onDismissRequest = viewModel::closeDialog,
            primaryButtonText = stringResource(Res.string.generic_delete),
            secondaryButtonText = stringResource(Res.string.generic_cancel)
        ) {
            viewModel.deleteItem(it.id, it.type)
            viewModel.closeDialog()
        }
    }

    bottomSheetState?.let {
        when (it) {
            is BottomSheetState.CurrencySelectionState -> {
                SearchBottomSheet(
                    sheetState = modalBottomSheetState,
                    onDismiss = { viewModel.resetBottomSheetState() },
                    searchFieldLabel = stringResource(Res.string.text_field_search_currency_hint),
                    items = it.currencyCodes.map { currencyCode ->
                        object : SearchItem {
                            override val key: String
                                get() = currencyCode
                        }
                    },
                ) { _, item ->
                    BottomSheetItem(
                        name = item.key,
                    ) {
                        viewModel.onBottomSheetItemSelected(item.key)
                    }
                }
            }
        }
    }

    SGSnackBar(
        snackBarHostState = snackBarHostState,
        snackBarType = if (snackBarState is SnackBarState.Error) SnackBarType.Error else SnackBarType.Success
    )
}

@Composable
private fun MainContentScreen(
    modifier: Modifier = Modifier,
    viewState: AccountEditorViewState,
    onSelectAccountGroup: (AccountGroup) -> Unit,
    onDeleteAccount: (String) -> Unit,
    onAddNewAccount: () -> Unit
) {
    val accountCount = stringResource(Res.string.text_field_search_currency_hint)
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        if (viewState.selectedAccountGroup == null) {
            viewState.accountGroups.forEachIndexed { index, it ->
                SelectionItem(
                    item = it,
                    title = { it.accountGroupName },
                    subtitle = { "${it.accounts.size} $accountCount" },
                    onClick = { onSelectAccountGroup(it) },
                    showDivider = index > 0,
                    suffixIcon = { SGIcons.NextArrow() }
                )
            }
        } else {
            if (viewState.selectedAccountGroup.accounts.isEmpty()) {
                NoResultContent(
                    modifier = Modifier.weight(1f),
                    stringResource(Res.string.no_available_account_hint)
                )
            } else {
                viewState.selectedAccountGroup.accounts.forEachIndexed { index, account ->
                    SelectionItem(
                        item = account,
                        title = { it.accountName },
                        onSuffixIconClicked = { onDeleteAccount(it.accountId.toString()) },
                        showDivider = index > 0,
                        suffixIcon = { SGIcons.Delete() }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            SGLargePrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.button_add_new_account)
            ) {
                onAddNewAccount()
            }
        }
    }
}

@Composable
private fun AccountGroupItem(
    accountGroup: AccountGroup,
    onClick: () -> Unit,
    showDivider: Boolean
) {
    val accountCount = stringResource(Res.string.accounts_count)
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = accountGroup.accountGroupName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${accountGroup.accounts.size} $accountCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SGIcons.NextArrow()
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun AccountItem(
    account: AccountGroup.Account,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.accountName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = account.currency,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onDelete() },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    SGIcons.Delete()
                }
            }
        }
    }
}

@Composable
private fun EditorScreen(
    modifier: Modifier = Modifier,
    editorLabel: String,
    viewState: AccountEditorViewState,
    updateAccountName: (String) -> Unit,
    updateAmount: (String) -> Unit,
    visualTransformation: CurrencyVisualTransformation,
    onFieldTap: (TapFieldType) -> Unit,
    addNewItem: () -> Unit,
) {
    val outputCurrencyFormatter = remember { CurrencyTextFieldOutputFormatter() }

    EditorCard(
        modifier = Modifier.fillMaxSize(),
        header = stringResource(Res.string.generic_account_details),
        buttonText = stringResource(Res.string.button_create_account),
        onButtonClicked = addNewItem
    ) {
        SGTextField(
            modifier = Modifier.fillMaxWidth(),
            label = editorLabel,
            value = when (val vs = viewState.editorState) {
                is AccountEditorViewState.AccountEditorState -> vs.accountName
                null -> ""
            }
        ) {
            updateAccountName(it)
        }

        if (viewState.editorState is AccountEditorViewState.AccountEditorState) {
            SGTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.account_editor_currency_label),
                value = viewState.editorState.currency,
                readOnly = true,
                onClicked = { onFieldTap(TapFieldType.CurrencyField) }
            ) {}

            SGTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.account_editor_amount_label),
                value = viewState.editorState.amount,
                keyboardType = KeyboardType.Number,
                visualTransformation = visualTransformation,
                outputFormatter = { outputCurrencyFormatter.format(it) }
            ) {
                updateAmount(it)
            }
        }
    }
}