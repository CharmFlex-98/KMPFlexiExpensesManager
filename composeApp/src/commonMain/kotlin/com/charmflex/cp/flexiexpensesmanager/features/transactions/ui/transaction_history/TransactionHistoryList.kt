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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody1
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.ListTable
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x7
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x8
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TransactionHistoryList(
    modifier: Modifier = Modifier,
    transactionHistoryViewModel: TransactionHistoryViewModel,
) {
    val viewState by transactionHistoryViewModel.viewState.collectAsState()
    val scrollItems = viewState.items
    val scrollState = rememberLazyListState()
    val tabScrollState = rememberScrollState()
    val tabState by transactionHistoryViewModel.tabState.collectAsState()
    val tabHeight = with(LocalDensity.current) { grid_x7.roundToPx().toFloat() }
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
                .fillMaxSize()
                .padding(vertical = grid_x2),
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
                SecondaryScrollableTabRow(
                    selectedTabIndex = tabState.selectedTabIndex,
                    scrollState = tabScrollState,
                ) {
                    tabState.tabs.forEachIndexed { index, item ->
                        Tab(
                            modifier = Modifier
                                .height(grid_x8)
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
            ExpensesHistoryItem(
                id = it.id,
                name = it.name,
                amount = it.bankTransactionAmount,
                category = it.category,
                type = it.type,
                iconResId = it.iconResId,
                onClick = {
                    onClick(it)
                }
            )
            if (index != items.size - 1) HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
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
    onClick: (Long) -> Unit
) {
    val signedAmount = when (type) {
        TransactionType.EXPENSES.name -> "-$amount"
        TransactionType.INCOME.name -> "+$amount"
        else -> amount
    }
    val signAmountColor = when (type) {
        TransactionType.EXPENSES.name -> Color.Red
        TransactionType.INCOME.name -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(id)
            }
            .background(
                MaterialTheme.colorScheme.surfaceContainerHighest
            )
            .padding(vertical = grid_x2, horizontal = grid_x1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(grid_x1),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(grid_x4),
                painter = painterResource(iconResId),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = grid_x0_5),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = name,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(grid_x1))
            Text(
                text = category.ifEmpty { if (type == TransactionType.TRANSFER.name) "TRANSFER" else if (type == TransactionType.UPDATE_ACCOUNT.name) "UPDATE ACCOUNT" else "UNKNOWN" },
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier.padding(grid_x1),
            contentAlignment = Alignment.Center
        ) {
            FEBody1(text = signedAmount, color = signAmountColor)
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