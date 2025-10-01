package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.core.domain.FEField
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyTextFieldOutputFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DATE_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.toLocalDate
import com.charmflex.cp.flexiexpensesmanager.core.utils.toStringWithPattern
import com.charmflex.cp.flexiexpensesmanager.features.account.domain.model.AccountGroup
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.models.TransactionCategories
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.SchedulerPeriod
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TRANSACTION_AMOUNT
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TRANSACTION_FROM_ACCOUNT
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading5
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGButtonGroupVertical
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGDatePicker
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargeSecondaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGModalBottomSheet
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSmallPrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGTextField
import com.charmflex.cp.flexiexpensesmanager.ui_common.SearchBottomSheet
import com.charmflex.cp.flexiexpensesmanager.ui_common.SearchItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.SupportingText
import com.charmflex.cp.flexiexpensesmanager.ui_common.SupportingTextType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_25
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x20
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun TransactionEditorScreen(
    viewModel: TransactionEditorBaseViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val currencyExchangeViewState by viewModel.combinedCurrencyExchangeViewState.collectAsState()
    val currentTransactionType by viewModel.currentTransactionType.collectAsState()
    val showCalendar = viewState.calendarState != null
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarState by viewModel.snackBarState
    val genericErrorMessage = stringResource(Res.string.generic_error)
    val bottomSheetState = rememberModalBottomSheetState()
    val currencyVisualTransformation = remember(viewState.currencyCode) {
        viewModel.currencyVisualTransformationBuilder().create(viewState.currencyCode)
    }
    val outputCurrencyFormatter = remember { CurrencyTextFieldOutputFormatter() }
    val type = viewModel.getType()
    val title = when (type) {
        TransactionRecordableType.NEW_TRANSACTION -> stringResource(Res.string.new_transaction_app_bar_title)
        TransactionRecordableType.EDIT_TRANSACTION -> stringResource(Res.string.edit_transaction_app_bar_title)
        TransactionRecordableType.NEW_SCHEDULER -> stringResource(Res.string.new_scheduler_app_bar_title)
        TransactionRecordableType.EDIT_SCHEDULER -> stringResource(Res.string.edit_scheduler_app_bar_title)
    }
    val actionTitle = when (type) {
        TransactionRecordableType.NEW_TRANSACTION, TransactionRecordableType.NEW_SCHEDULER -> stringResource(
            Res.string.general_congratz
        )

        else -> stringResource(Res.string.generic_success)
    }
    val actionSubtitle = when (type) {
        TransactionRecordableType.NEW_TRANSACTION -> stringResource(Res.string.create_new_transaction_success_dialog_subtitle)
        TransactionRecordableType.EDIT_TRANSACTION -> stringResource(Res.string.edit_new_transaction_success_dialog_subtitle)
        TransactionRecordableType.NEW_SCHEDULER -> stringResource(Res.string.create_new_scheduler_success_dialog_subtitle)
        TransactionRecordableType.EDIT_SCHEDULER -> stringResource(Res.string.edit_new_scheduler_success_dialog_subtitle)
    }
    var initLoader by remember { mutableStateOf(true) }
    val showDialogCurrencyState = currencyExchangeViewState.showDialogState

    LaunchedEffect(key1 = Unit) {
        delay(500)
        initLoader = false
    }

    LaunchedEffect(Unit) {
        viewModel.onRefreshOptions()
    }

    LaunchedEffect(snackBarState) {
        when (val state = snackBarState) {
            is SnackBarState.Error -> {
                snackbarHostState.showSnackBarImmediately(state.message ?: genericErrorMessage)
                viewModel.resetErrorState()
            }

            else -> {}
        }
    }

    SGScaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = title, style = TextStyle(fontSize = 20.sp)
                )
            }, navigationIcon = {
                IconButton(
                    onClick = { viewModel.onBack() }
                ) {
                    SGIcons.ArrowBack()
                }
            }
            )
        },
        screenName = "NewTransactionScreen",
        isLoading = viewState.isLoading || initLoader
    ) {
        if (initLoader.not()) Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(grid_x2)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center
                ) {
                    viewModel.transactionType.forEach {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FEBody3(
                                    text = stringResource(
                                        TransactionType.getStringRes(
                                            TransactionType.valueOf(it.name)
                                        )
                                    )
                                )
                                RadioButton(
                                    selected = currentTransactionType == it,
                                    onClick = {
                                        viewModel.onTransactionTypeChanged(it)
                                    }
                                )
                            }
                        }
                    }
                }
                viewState.fields.forEach { feField ->
                    val suffixText: String? = when (feField.id) {
                        TRANSACTION_AMOUNT -> {
                            currencyExchangeViewState.transactionCurrencyViewState?.toCurrencyAmountFormatted
                        }

                        TRANSACTION_FROM_ACCOUNT -> {
                            currencyExchangeViewState.accountCurrencyViewState?.toCurrencyAmountFormatted
                        }

                        else -> null
                    }
                    val supportingText = when (feField.id) {
                        TRANSACTION_AMOUNT -> {
                            currencyExchangeViewState.transactionCurrencyViewState?.rate

                        }


                        TRANSACTION_FROM_ACCOUNT -> {
                            currencyExchangeViewState.accountCurrencyViewState?.rate
                        }

                        else -> null
                    }
                    val onSuffixTextClicked = {
                        when (feField.id) {
                            TRANSACTION_AMOUNT -> {
                                viewModel.onCurrencyViewTapped(currencyExchangeViewState.transactionCurrencyViewState)
                            }

                            TRANSACTION_FROM_ACCOUNT -> {
                                viewModel.onCurrencyViewTapped(currencyExchangeViewState.accountCurrencyViewState)
                            }

                            else -> {}
                        }
                    }
                    when (val type = feField.type) {
                        is FEField.FieldType.Callback -> {
                            SGTextField(
                                modifier = Modifier
                                    .padding(vertical = grid_x1)
                                    .fillMaxWidth(),
                                label = stringResource(feField.labelId),
                                hint = stringResource(feField.hintId),
                                value = feField.valueItem.value,
                                readOnly = true,
                                onValueChange = {},
                                onClicked = {
                                    viewModel.onCallbackFieldTap(feField)
                                },
                                trailingIcon = if (feField.allowClear) {
                                    {
                                        IconButton(onClick = {
                                            viewModel.onClearField(feField)
                                        }) {
                                            SGIcons.Close()
                                        }
                                    }
                                } else null,
                                suffixText = suffixText,
                                onSuffixTextClicked = onSuffixTextClicked,
                                supportingText = supportingText?.let {
                                    SupportingText(
                                        it,
                                        SupportingTextType.INFO
                                    )
                                }
                            )
                        }

                        else -> {
                            SGTextField(
                                modifier = Modifier
                                    .padding(vertical = grid_x1)
                                    .fillMaxWidth(),
                                label = stringResource(feField.labelId),
                                value = feField.valueItem.value,
                                hint = stringResource(feField.hintId),
                                enable = feField.isEnable,
                                visualTransformation = if (feField.type is FEField.FieldType.Currency) {
                                    currencyVisualTransformation
                                } else VisualTransformation.None,
                                keyboardType = if (type is FEField.FieldType.Number || type is FEField.FieldType.Currency) KeyboardType.Number else KeyboardType.Text,
                                outputFormatter = if (feField.type is FEField.FieldType.Currency) {
                                    { outputCurrencyFormatter.format(it) }
                                } else null,
                                suffixText = suffixText,
                                onSuffixTextClicked = onSuffixTextClicked,
                                supportingText = supportingText?.let {
                                    SupportingText(
                                        it,
                                        SupportingTextType.INFO
                                    )
                                }
                            ) { newValue ->
                                viewModel.onFieldValueChanged(
                                    feField,
                                    newValue,
                                    updateMonetary = feField.shouldResetMonetary()
                                )
                            }
                        }
                    }
                }
            }
            SGButtonGroupVertical {
                SGLargePrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.new_expenses_confirm_button),
                    enabled = viewModel.allowProceed()
                ) {
                    viewModel.onConfirmed()
                }
                SGLargeSecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.new_expenses_cancel_button)
                ) {
                    viewModel.onBack()
                }
            }
        }
    }

    SGDatePicker(
        onDismiss = { viewModel.onToggleCalendar(null) },
        onConfirm = {
            viewModel.onFieldValueChanged(
                viewState.calendarState?.targetField,
                it.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN),
            )
            viewModel.onToggleCalendar(null)
        },
        date = viewState.calendarState?.targetField?.valueItem?.value?.toLocalDate(
            DATE_ONLY_DEFAULT_PATTERN
        ),
        isVisible = showCalendar,
        boundary = viewModel.calendarSelectionRange()
    )

    if (viewState.success) {
        SGActionDialog(
            title = actionTitle,
            text = actionSubtitle,
            onDismissRequest = { },
            primaryButtonText = stringResource(Res.string.generic_back_to_home)
        ) {
            viewModel.onBack()
        }
    }

    if (showDialogCurrencyState != null) {
        val currencyVisualTransformation2 = remember(showDialogCurrencyState.toCurrency) {
            viewModel.currencyVisualTransformationBuilder()
                .create(showDialogCurrencyState.toCurrency)
        }
        SGDialog(
            title = stringResource(Res.string.generic_exchange_rate_hint),
            subtitle = "",
            onDismissRequest = {}
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.End
            ) {
                SGTextField(
                    modifier = Modifier,
                    keyboardType = KeyboardType.Number,
                    value = showDialogCurrencyState.toCurrencyAmount,
                    label = stringResource(Res.string.generic_amount),
                    hint = stringResource(Res.string.generic_amount),
                    visualTransformation = currencyVisualTransformation2,
                    outputFormatter = { outputCurrencyFormatter.format(it) },
                    onValueChange = {
                        viewModel.onCurrencyExchangeAmountChanged(it, showDialogCurrencyState)
                    })
                SGTextField(
                    modifier = Modifier,
                    value = showDialogCurrencyState.rate,
                    label = stringResource(Res.string.generic_rate),
                    hint = stringResource(Res.string.generic_rate),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        viewModel.onCurrencyExchangeRateChanged(it, showDialogCurrencyState)
                    })
                Spacer(modifier = Modifier.size(grid_x4))
                SGSmallPrimaryButton(text = stringResource(Res.string.button_done)) {
                    viewModel.onCurrencyViewClosed(showDialogCurrencyState)
                }
            }
        }
    }

    if (viewState.showBottomSheet) {
        val state = viewState.bottomSheetState
        if (state is TransactionEditorViewState.CurrencySelectionBottomSheetState) {
            SearchBottomSheet(
                sheetState = bottomSheetState,
                onDismiss = { viewModel.toggleBottomSheet(null) },
                searchFieldLabel = stringResource(Res.string.text_field_search_currency_hint),
                items = viewState.currencyList.map {
                    object : SearchItem {
                        override val key: String
                            get() = it
                    }
                },
            ) { _, item ->
                BottomSheetItem(
                    name = item.key,
                ) {
                    viewModel.onCurrencySelected(item.key, state.feField)
                }
            }
        } else {
            SGModalBottomSheet(
                modifier = Modifier.padding(grid_x2),
                sheetState = bottomSheetState,
                onDismiss = { viewModel.toggleBottomSheet(null) }
            ) {
                when (val bs = viewState.bottomSheetState) {
                    is TransactionEditorViewState.CategorySelectionBottomSheetState -> {
                        CategorySelectionBottomSheet(
                            onSelected = { id, name ->
                                viewModel.onCategorySelected(id, name, bs.feField)
                                viewModel.toggleBottomSheet(null)
                            },
                            transactionCategories = viewState.transactionCategories,
                            onEdit = { viewModel.onBottomSheetOptionsEdit(bs) }
                        )
                    }

                    is TransactionEditorViewState.AccountSelectionBottomSheetState -> {
                        val accountCountText = stringResource(Res.string.accounts_count)
                        if (bs.selectedAccountGroup == null) {
                            EnhancedGeneralSelectionContent(
                                items = viewState.accountGroups,
                                getName = { it.accountGroupName },
                                getSubtitle = { "${it.accounts.size} $accountCountText" },
                                suffixIcon = { Res.drawable.ic_arrow_next },
                                headerContent = {
                                    BottomSheetHeaderContent(stringResource(Res.string.generic_account_group)) {
                                        viewModel.onBottomSheetOptionsEdit(bs)
                                    }
                                }
                            ) {
                                val newState = bs.copy(selectedAccountGroup = it)
                                viewModel.toggleBottomSheet(newState)
                            }
                        } else {
                            EnhancedGeneralSelectionContent(
                                items = bs.selectedAccountGroup.accounts,
                                getName = { it.accountName },
                                headerContent = {
                                    BottomSheetHeaderContent(stringResource(Res.string.generic_account)) {
                                        viewModel.onBottomSheetOptionsEdit(bs)
                                    }
                                }
                            ) {
                                viewModel.onSelectAccount(it, bs.feField)
                                viewModel.toggleBottomSheet(null)
                            }
                        }
                    }

                    is TransactionEditorViewState.TagSelectionBottomSheetState -> {
                        EnhancedGeneralSelectionContent(
                            items = viewState.tagList,
                            getName = { it.name },
                            headerContent = {
                                BottomSheetHeaderContent(stringResource(Res.string.setting_tag)) {
                                    viewModel.onBottomSheetOptionsEdit(bs)
                                }
                            },
                        ) {
                            viewModel.onTagSelected(it, bs.feField)
                            viewModel.toggleBottomSheet(null)
                        }
                    }

                    is TransactionEditorViewState.PeriodSelectionBottomSheetState -> {
                        EnhancedGeneralSelectionContent(
                            items = viewModel.scheduledPeriodType,
                            getName = {
                                stringResource(
                                    SchedulerPeriod.getStringRes(
                                        SchedulerPeriod.valueOf(
                                            it.name
                                        )
                                    )
                                )
                            },
                            headerContent = {
                                FEHeading5(text = stringResource(Res.string.generic_period))
                            },
                        ) {
                            viewModel.onPeriodSelected(it, bs.feField)
                            viewModel.toggleBottomSheet(null)
                        }
                    }

                    is TransactionEditorViewState.UpdateTypeSelectionBottomSheetState -> {
                        EnhancedGeneralSelectionContent(
                            items = viewModel.updateAccountType,
                            getName = {
                                stringResource(
                                    UpdateAccountType.getStringRes(
                                        UpdateAccountType.valueOf(it.name)
                                    )
                                )
                            },
                            headerContent = {
                                FEHeading5(text = stringResource(Res.string.generic_type))
                            },
                        ) {
                            viewModel.onFieldValueChanged(bs.feField, it.name)
                            viewModel.toggleBottomSheet(null)
                        }
                    }

                    else -> {}
                }

            }
        }
    }
    SGSnackBar(snackBarHostState = snackbarHostState, snackBarType = SnackBarType.Error)
}

@Composable
internal fun CategorySelectionBottomSheet(
    onSelected: (String, String) -> Unit,
    transactionCategories: TransactionCategories?,
    onEdit: () -> Unit,
) {
    val list = remember {
        mutableStateListOf(transactionCategories?.items)
    }
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(list.size) {
        horizontalScrollState.animateScrollBy(500f)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        BottomSheetHeaderContent(stringResource(Res.string.generic_category)) {
            onEdit()
        }
        Row(
            modifier = Modifier
                .padding(top = grid_x2)
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState)
        ) {
            list.forEach {
                CategoryList(
                    categoryList = it ?: listOf(),
                    onCategorySelected = {
                        onSelected(
                            it.categoryId.toString(),
                            it.categoryName
                        )
                    },
                    onToggleChildren = {
                        if (it.children.isEmpty().not()) {
                            if (it.level < list.size) {
                                list.removeRange(it.level, list.size)
                            }
                            list.add(it.children)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    categoryList: List<TransactionCategories.BasicCategoryNode>,
    onCategorySelected: (TransactionCategories.BasicCategoryNode) -> Unit,
    onToggleChildren: (TransactionCategories.BasicCategoryNode) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
    ) {
        categoryList.forEach {
            Box(
                modifier = Modifier
                    .width(grid_x20)
                    .border(width = 0.5.dp, color = Color.Black, shape = RectangleShape)
                    .clickable {
                        onCategorySelected(it)
                    }
                    .padding(grid_x2)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FEMetaData1(
                        modifier = Modifier.weight(1f),
                        text = it.categoryName
                    )
                    if (!it.isLeaf) {
                        IconButton(
                            modifier = Modifier.size(grid_x2),
                            onClick = { onToggleChildren(it) }) {
                            SGIcons.NextArrow()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun <T> EnhancedGeneralSelectionContent(
    modifier: Modifier = Modifier,
    items: List<T>,
    getName: @Composable (T) -> String,
    getSubtitle: ((T) -> String)? = null,
    getIcon: ((T) -> DrawableResource)? = null,
    suffixIcon: ((T) -> DrawableResource?) = { null },
    headerContent: @Composable (BoxScope.() -> Unit)? = null,
    headerContentAlignment: Alignment = Alignment.TopStart,
    onSelectItem: (T) -> Unit
) {
    Column {
        Box(modifier = modifier.padding(vertical = grid_x2), headerContentAlignment) {
            headerContent?.invoke(this)
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(grid_x1),
            modifier = Modifier.heightIn(max = 400.dp)
        ) {
            items(items) { item ->
                BottomSheetItem(
                    name = getName(item),
                    subtitle = getSubtitle?.invoke(item),
                    icon = getIcon?.invoke(item),
                    suffixIcon = suffixIcon.invoke(item),
                    onItemSelected = { onSelectItem(item) }
                )
            }
        }
    }
}

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    name: String,
    subtitle: String? = null,
    icon: DrawableResource? = null,
    suffixIcon: DrawableResource? = null,
    onItemSelected: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onItemSelected() }
            .padding(grid_x0_25),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Surface(
                    modifier = Modifier.size(grid_x2_5),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(grid_x1_5),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(grid_x1_5))
            }

            Column(
                modifier = Modifier.weight(1f),
            ) {
                FEBody2(
                    text = name,
                    color = MaterialTheme.colorScheme.primary
                )

                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(grid_x0_5))
                    FEMetaData1(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }


            if (suffixIcon != null) {
                Surface(
                    modifier = Modifier.size(grid_x2),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = modifier,
                            painter = painterResource(suffixIcon),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetHeaderContent(
    title: String,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FEHeading4(text = title)
        Spacer(modifier = Modifier.weight(1f))
        SGSmallPrimaryButton(text = stringResource(Res.string.button_edit)) {
            onClicked()
        }
    }
}