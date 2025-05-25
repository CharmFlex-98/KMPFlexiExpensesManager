package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.core.utils.DATE_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.MONTH_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.YEAR_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.toLocalDate
import com.charmflex.cp.flexiexpensesmanager.core.utils.toStringWithPattern
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.mapper.TransactionHistoryMapper
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource

internal abstract class TransactionHistoryViewModel(
    private val mapper: TransactionHistoryMapper,
    private val routeNavigator: RouteNavigator
) : ViewModel() {
    var job: Job = SupervisorJob()
        private set
        get() {
            if (field.isCancelled) field = SupervisorJob()
            return field
        }

    private val _tabState = MutableStateFlow(TabState())
    val tabState = _tabState.asStateFlow()

    private val _offset = MutableStateFlow(0L)
    val offset = _offset.asStateFlow()

    private val _viewState = MutableStateFlow(TransactionHistoryViewState())
    val viewState = _viewState.asStateFlow()

    private val _scrollToIndexEvent: MutableSharedFlow<Int> = MutableSharedFlow(extraBufferCapacity = 1)
    val scrollToIndexEvent = _scrollToIndexEvent.asSharedFlow()

    private fun toggleLoadMoreLoader(isLoadMore: Boolean) {
        _viewState.update {
            it.copy(
                isLoadMore = isLoadMore
            )
        }
    }

    private fun toggleLoader(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun observeTransactionList() {
        toggleLoader(true)
        viewModelScope.launch(job) {
            getDBTransactionListFlow(offset = 0)
                .catch {
                    toggleLoader(false)
                }
                .collectLatest { list ->
                    _offset.value = list.size.toLong()
                    val filteredData = filter(list)
                    onReceivedFilteredData(filteredData, clearOldList = true)
                    updateList(list = filteredData)
                }
        }
    }

//    fun onChartItemSelected(markerEntryModel: LottieClipSpec.Marker.EntryModel?) {
////        markerEntryModel?.index?.let { _scrollToIndexEvent.tryEmit(it) }
//    }

    // This must be called in the init block of children classes.
    open fun refresh() {
        job.cancel()
        observeTransactionList()
    }

    abstract fun getDBTransactionListFlow(offset: Long): Flow<List<Transaction>>

    abstract suspend fun filter(dbData: List<Transaction>): List<Transaction>

    abstract suspend fun onReceivedFilteredData(data: List<Transaction>, clearOldList: Boolean)

    fun getNextTransactions() {
        toggleLoadMoreLoader(true)
        viewModelScope.launch {
            delay(1000) // mimic fetching data :)
            getDBTransactionListFlow(offset = offset.value)
                .catch {
                    // TODO: Need to do something?
                    toggleLoadMoreLoader(false)
                }
                .firstOrNull()?.let { list ->
                    _offset.value += list.size
                    val filteredData = filter(list)
                    onReceivedFilteredData(filteredData, clearOldList = false)
                    updateList(list = filteredData, clearOldList = false)
                }
        }
    }

    private fun updateList(list: List<Transaction>, clearOldList: Boolean = true) {
        val updatedList = mapper.map(list)
        _viewState.update { it ->
            it.copy(
                items = if (clearOldList) updatedList else appendsTransactionHistoryItems(
                    updatedList
                ),
                isLoading = false,
                isLoadMore = false
            )
        }
        updateTabList(_viewState.value.items)
    }

    private fun appendsTransactionHistoryItems(appendItems: List<TransactionHistoryItem>): List<TransactionHistoryItem> {
        return _viewState.value.items.toMutableList()
            .apply ori@{
                val duplicateDateHeader =
                    this.lastOrNull { it is TransactionHistoryHeader }?.dateKey?.let {
                        it == appendItems.firstOrNull { it is TransactionHistoryHeader }?.dateKey
                    } ?: false
                val newUpdatedList = appendItems.toMutableList().apply {
                    if (duplicateDateHeader) {
                        // Remove new list first date header
                        removeAt(0)
                        // Remove the new list first content
                        val toMergeItems = (removeAt(0) as TransactionHistorySection).items

                        // Modifier previous section items
                        val lastSection = this@ori.removeLast() as TransactionHistorySection
                        val lastSectionMergedItems = lastSection.items.toMutableList().apply {
                            if (toMergeItems.isNotEmpty()) {
                                addAll(toMergeItems)
                            }
                        }
                        this@ori.add(
                            lastSection.copy(
                                dateKey = lastSection.dateKey,
                                items = lastSectionMergedItems
                            )
                        )
                    }
                }
                addAll(newUpdatedList)
            }
    }

    private fun updateTabList(items: List<TransactionHistoryItem>) {
        val tabs = items.map {
            val localDate = it.dateKey.toLocalDate(DATE_ONLY_DEFAULT_PATTERN)
            TabState.TabItem(
                year = localDate.toStringWithPattern(YEAR_ONLY_DEFAULT_PATTERN),
                month = localDate.toStringWithPattern(MONTH_ONLY_DEFAULT_PATTERN)
            )
        }
            .distinct()
            .reversed()

        _tabState.update {
            it.copy(
                tabs = tabs
            )
        }
    }

    fun findFirstItemIndexByTab(tab: TabState.TabItem): Int {
        return _viewState.value.items.indexOfFirst {
            val localDate = it.dateKey.toLocalDate(DATE_ONLY_DEFAULT_PATTERN)
            val year = localDate.toStringWithPattern(YEAR_ONLY_DEFAULT_PATTERN)
            val month = localDate.toStringWithPattern(MONTH_ONLY_DEFAULT_PATTERN)
            tab.month == month && tab.year == year
        }
    }

    fun onReachHistoryItem(item: TransactionHistoryItem?) {
        if (item == null) return

        val localDate = item.dateKey.toLocalDate(DATE_ONLY_DEFAULT_PATTERN)
        val year = localDate.toStringWithPattern(YEAR_ONLY_DEFAULT_PATTERN)
        val month = localDate.toStringWithPattern(MONTH_ONLY_DEFAULT_PATTERN)
        _tabState.update {
            it.copy(
                selectedTabIndex = it.tabs.indexOfFirst { tabItem ->
                    tabItem.year == year && tabItem.month == month
                }
            )
        }
    }

    fun onNavigateTransactionDetail(transactionId: Long) {
        routeNavigator.navigateTo(TransactionRoute.transactionDetailDestination(transactionId))
    }
}


internal data class TransactionHistoryViewState(
    val items: List<TransactionHistoryItem> = listOf(),
    val isLoading: Boolean = false,
    val isLoadMore: Boolean = false,
)

internal data class TabState(
    val selectedTabIndex: Int = 0,
    val tabs: List<TabItem> = listOf(),
) {
    data class TabItem(
        val year: String,
        val month: String
    )
}

internal sealed interface TransactionHistoryItem {
    val dateKey: String
}

internal data class TransactionHistoryHeader(
    override val dateKey: String,
    val title: String?,
    val subtitle: String
) : TransactionHistoryItem

internal data class TransactionHistorySection(
    override val dateKey: String,
    val items: List<SectionItem>
) : TransactionHistoryItem {
    data class SectionItem(
        val id: Long,
        val name: String,
        val amount: String,
        val category: String,
        val type: String,
        val iconResId: DrawableResource
    )
}