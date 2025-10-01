package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history

import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TRANSACTION_TAG
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody1
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.ListTable
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x7
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x8
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_error
import kotlinproject.composeapp.generated.resources.generic_expenses
import kotlinproject.composeapp.generated.resources.generic_income
import kotlinproject.composeapp.generated.resources.generic_transfer
import kotlinproject.composeapp.generated.resources.generic_unknown_capital
import kotlinproject.composeapp.generated.resources.generic_update
import kotlinproject.composeapp.generated.resources.generic_update_account
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionHistoryList(
    modifier: Modifier = Modifier,
    transactionHistoryViewModel: TransactionHistoryViewModel,
) {
    val viewState by transactionHistoryViewModel.viewState.collectAsState()
    val scrollItems = viewState.items
    val scrollState = rememberLazyListState()
    val tabState by transactionHistoryViewModel.tabState.collectAsState()
    val tabPx = grid_x7
    val tabHeight = with(LocalDensity.current) { tabPx.roundToPx().toFloat() }
    val showMonthTab by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex > 2 }
    }
    val diff = with(LocalDensity.current) { grid_x2.roundToPx().toFloat() }
    val firstVisibleItemIndex by remember {
        derivedStateOf {
            scrollState.layoutInfo.visibleItemsInfo.lastOrNull {
                it.offset < (tabHeight)
            }?.index ?: 0
        }
    }
    val isScrollingUp = scrollState.isScrollingUp()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        transactionHistoryViewModel.scrollToIndexEvent.collectLatest {
            scrollState.animateScrollToItem(it)
        }
    }

    LaunchedEffect(firstVisibleItemIndex, isScrollingUp) {
        transactionHistoryViewModel.onReachHistoryItem(scrollItems.getOrNull(firstVisibleItemIndex))
    }

    LaunchedEffect(key1 = viewState.isLoading) {
        if (!viewState.isLoading.not()) {
            scrollState.animateScrollToItem(0)
        }
    }

    Box {
        ListTable(
            modifier = modifier
                .fillMaxSize(),
            items = scrollItems,
            scrollState = scrollState,
            onLoadMore = { transactionHistoryViewModel.getNextTransactions() },
            isLoadMore = viewState.isLoadMore,
        ) { index, item ->
            when (item) {
                is TransactionHistoryHeader -> {
                    Spacer(modifier = Modifier.height(grid_x2))
                    ExpensesHistoryDateHeaderView(month = item.title, date = item.subtitle)
                }

                is TransactionHistorySection -> ExpensesHistorySectionView(items = item.items) { id ->
                    transactionHistoryViewModel.onNavigateTransactionDetail(id)
                }
            }
        }

        if (showMonthTab) {
            SGAnimatedTransition(
                enter = slideInVertically(initialOffsetY = { -it }) +
                        expandVertically(expandFrom = Alignment.Top),
                exit = slideOutVertically(targetOffsetY = { -it }) +
                        shrinkVertically(shrinkTowards = Alignment.Top),
            ) {
                ScrollableTabRow(
                    selectedTabIndex = tabState.selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    tabState.tabs.forEachIndexed { index, item ->
                        Tab(
                            modifier = Modifier
                                .height(tabPx)
                                .padding(grid_x1),
                            selected = index == tabState.selectedTabIndex,
                            onClick = {
                                coroutineScope.launch {
                                    val scrollToIndex =
                                        transactionHistoryViewModel.findFirstItemIndexByTab(item)
                                    scrollState.scrollToItem(scrollToIndex)
                                    scrollState.scrollBy(-tabHeight + diff)
                                }
                            }
                        ) {
                            FEBody3(text = item.year)
                            FEBody2(text = item.month)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpensesHistoryDateHeaderView(
    month: String? = null,
    date: String
) {
    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.wrapContentWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            month?.let { FEBody1(text = it) }
            FEMetaData1(text = date, color = Color.Gray)
        }
    }
}

@Composable
private fun ExpensesHistorySectionView(
    items: List<TransactionHistorySection.SectionItem>,
    onClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(grid_x2))
            .padding(vertical = grid_x1)
    ) {
        items.forEachIndexed { index, it ->
            val typeName = when (TransactionType.valueOf(it.type)) {
                TransactionType.EXPENSES -> stringResource(Res.string.generic_expenses)
                TransactionType.INCOME -> stringResource(Res.string.generic_income)
                TransactionType.TRANSFER -> stringResource(Res.string.generic_transfer)
                TransactionType.UPDATE_ACCOUNT -> stringResource(Res.string.generic_update_account)
            }
            ExpensesHistoryItem(
                id = it.id,
                name = it.name.ifEmpty { typeName },
                amount = it.bankTransactionAmount,
                category = it.category,
                type = it.type,
                iconResId = it.iconResId,
                onClick = {
                    onClick(it)
                }
            )
        }
    }
}

@Composable
internal fun ExpensesHistoryItem(
    modifier: Modifier = Modifier,
    id: Long,
    name: String,
    amount: String,
    category: String,
    type: String,
    iconResId: DrawableResource,
    suffixIcon: @Composable (() -> Unit)? = null,
    onIconClicked: (() -> Unit)? = null,
    onClick: (Long) -> Unit
) {
    val signedAmount = when (type) {
        TransactionType.EXPENSES.name -> "-$amount"
        TransactionType.INCOME.name -> "+$amount"
        else -> amount
    }
    val signAmountColor = when (type) {
        TransactionType.EXPENSES.name -> Color(0xFFD32F2F) // Darker red
        TransactionType.INCOME.name -> Color(0xFF388E3C)   // Darker green
        TransactionType.TRANSFER.name -> Color(0xFF1976D2) // Darker blue
        TransactionType.UPDATE_ACCOUNT.name -> Color(0xFF1976D2) // Darker blue
        else -> MaterialTheme.colorScheme.onSurface
    }
    val iconBackgroundColor = signAmountColor.copy(alpha = 0.1f)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = grid_x1, vertical = grid_x0_5)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.4f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(grid_x2)
            )
            .clickable { onClick(id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(grid_x2)
        ) {
            // Icon with subtle background
            Box(
                modifier = Modifier
                    .size(grid_x4)
                    .background(
                        color = iconBackgroundColor,
                        shape = RoundedCornerShape(grid_x1)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(grid_x2),
                    painter = painterResource(iconResId),
                    contentDescription = null,
                    tint = signAmountColor
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Transaction Name
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Category with type badge if needed
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(grid_x1)
                ) {
                    val displayCategory = category.ifEmpty { 
                        when (type) {
                            TransactionType.TRANSFER.name -> "Transfer"
                            TransactionType.UPDATE_ACCOUNT.name -> "Account Update"
                            else -> "No Category"
                        }
                    }
                    
                    Text(
                        text = displayCategory,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    
                    // Show type badge for special transaction types
                    if (type == TransactionType.TRANSFER.name || type == TransactionType.UPDATE_ACCOUNT.name) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = signAmountColor.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = when (type) {
                                    TransactionType.TRANSFER.name -> stringResource(Res.string.generic_transfer)
                                    TransactionType.UPDATE_ACCOUNT.name -> stringResource(Res.string.generic_update)
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = signAmountColor
                            )
                        }
                    }
                }
            }
            
            // Amount with better styling
            Text(
                text = signedAmount,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = signAmountColor
            )
            
            // Suffix icon if provided
            if (suffixIcon != null) {
                IconButton(
                    onClick = { onIconClicked?.invoke() },
                    modifier = Modifier.size(grid_x4)
                ) {
                    suffixIcon()
                }
            }
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}