package com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.stat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.usecases.GetEachRootCategoryAmountUseCase
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.koin.core.annotation.Factory
import kotlin.math.round

@Factory
internal class CategoryStatViewModel constructor(
    private val getEachRootCategoryAmountUseCase: GetEachRootCategoryAmountUseCase,
    private val currencyFormatter: CurrencyFormatter,
    private val routeNavigator: RouteNavigator,
    val userCurrencyRepository: UserCurrencyRepository,
) : ViewModel() {
    private var job = SupervisorJob()
        get() {
            if (field.isCancelled) field = SupervisorJob()
            return field
        }
    private val _dateFilter = MutableStateFlow<DateFilter>(DateFilter.Monthly(0))
    val dateFilter = _dateFilter.asStateFlow()


    private val _viewState = MutableStateFlow(CategoryStatViewState())
    val viewState = _viewState.asStateFlow()

    private lateinit var _primaryCurrency: String

    init {
        viewModelScope.launch {
            _primaryCurrency = userCurrencyRepository.getPrimaryCurrency()
            observeDateFilter()
        }
    }

    fun onNavigateCategoryTransactionDetailScreen(
        categoryId: Int,
        categoryName: String,
        type: TransactionType
    ) {
        routeNavigator.navigateTo(
            CategoryRoutes.categoryTransactionDetail(
                categoryId,
                categoryName,
                type,
                _dateFilter.value
            ))
    }

    private fun observeDateFilter() {
        viewModelScope.launch {
            _dateFilter.collectLatest {
                refresh()
            }
        }
    }

    private fun refresh() {
        job.cancel()
        observeCategoryStats(TransactionType.EXPENSES)
        observeCategoryStats(TransactionType.INCOME)
    }

    private fun getNullContentState(): CategoryStatViewState.OverallCategoryData {
        return CategoryStatViewState.OverallCategoryData(
            stats = listOf(),
            totalAmountInCent = 0,
            amount = currencyFormatter.format(0, _primaryCurrency)
        )
    }

    private fun updateCategoryContentState(
        amount: Long,
        stats: List<CategoryStatViewState.CategoryStat>
    ): CategoryStatViewState.OverallCategoryData {
        return CategoryStatViewState.OverallCategoryData(
            stats = stats,
            totalAmountInCent = amount,
            amount = currencyFormatter.format(amount, _primaryCurrency)
        )
    }

    private fun observeCategoryStats(type: TransactionType) {
        toggleLoader(true)
        viewModelScope.launch(job) {
            getEachRootCategoryAmountUseCase(
                dateFilter = _dateFilter.value,
                transactionType = type
            ).collectLatest { flow ->
                val totalAmount =
                    flow?.values?.reduceOrNull { acc, l -> acc + l }
                if (totalAmount == null || totalAmount == 0L) {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            expensesCategoryStats = if (type == TransactionType.EXPENSES) getNullContentState() else it.expensesCategoryStats,
                            incomeCategoryStats = if (type == TransactionType.INCOME) getNullContentState() else it.incomeCategoryStats
                        )
                    }
                    return@collectLatest
                }
                val stats = flow.entries.map { res ->
                    val isPositive = res.value >= 0
                    val sign = if (isPositive) "" else "-"

                    CategoryStatViewState.CategoryStat(
                        id = res.key.id,
                        name = res.key.name,
                        isPositive = isPositive,
                        amount = "${sign}${
                            currencyFormatter.format(
                                res.value,
                                _primaryCurrency
                            )
                        }",
                        percentage = "${
                            round((res.value.toDouble() / totalAmount) * 100).toInt()
                        }%",
                        amountInCent = res.value,
                        type = type
                    )
                }.sortedByDescending { stat ->
                    stat.amountInCent
                }
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        expensesCategoryStats = if (type == TransactionType.EXPENSES) updateCategoryContentState(
                            totalAmount,
                            stats
                        ) else it.expensesCategoryStats,
                        incomeCategoryStats = if (type == TransactionType.INCOME) updateCategoryContentState(
                            totalAmount,
                            stats
                        ) else it.incomeCategoryStats
                    )
                }
            }
        }
    }

    fun onTabChanged(tab: CategoryStatTabItem) {
        _viewState.update {
            it.copy(
                selectedTab = tab
            )
        }
    }

    private fun toggleLoader(isLoading: Boolean) {
        _viewState.update {
            it.copy(isLoading = isLoading)
        }
    }

    fun onDateFilterChanged(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
    }
}

internal data class CategoryStatViewState(
    val expensesCategoryStats: OverallCategoryData = OverallCategoryData(),
    val incomeCategoryStats: OverallCategoryData = OverallCategoryData(),
    val isLoading: Boolean = false,
    val currencyCode: String = "",
    val selectedTab: CategoryStatTabItem = CategoryStatTabItem.EXPENSES
) {

    data class OverallCategoryData(
        val stats: List<CategoryStat> = listOf(),
        val amount: String = "0",
        val totalAmountInCent: Long = 0,
    )

    data class CategoryStat(
        val id: Int,
        val type: TransactionType,
        val name: String,
        val isPositive: Boolean,
        val amountInCent: Long,
        val amount: String,
        val percentage: String,
    )

    val categoryList
        get() = when (selectedTab) {
            CategoryStatTabItem.INCOME -> incomeCategoryStats.stats
            else -> expensesCategoryStats.stats
        }
}

internal enum class CategoryStatTabItem(val index: Int, val nameId: StringResource) {
    INCOME(index = 0, nameId = Res.string.category_stat_tab_income), EXPENSES(
        index = 1,
        nameId = Res.string.category_stat_tab_expenses
    )
}