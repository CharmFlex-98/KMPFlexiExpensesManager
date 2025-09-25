package com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BudgetRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.usecases.GetEachRootCategoryAmountUseCase
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.repositories.TagRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import kotlin.random.Random

@Factory
internal class ExpensesChartViewModel(
    private val getEachRootCategoryAmountUseCase: GetEachRootCategoryAmountUseCase,
    private val tagRepository: TagRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val userCurrencyRepository: UserCurrencyRepository,
    private val routeNavigator: RouteNavigator,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {

    private var job = SupervisorJob()
        get() {
            if (field.isCancelled) field = SupervisorJob()
            return field
        }

    private val _viewState = MutableStateFlow(ExpensesPieChartViewState())
    val viewState = _viewState.asStateFlow()

    private val _tagFilter = MutableStateFlow<List<TagFilterItem>>(emptyList())
    val tagFilter = _tagFilter.asStateFlow()

    private val _dateFilter: MutableStateFlow<DateFilter> = MutableStateFlow(DateFilter.Monthly(0))
    val dateFilter = _dateFilter.asStateFlow()

    init {
        loadTagOptions()
        observeTagFilterChanged()
        observeDateFilterChanged()
        refresh()
    }

    fun refresh() {
        job.cancel()
        viewModelScope.launch(job) {
            getEachRootCategoryAmountUseCase(
                dateFilter = _dateFilter.value,
                tagFilter = _tagFilter.value.filter { it.selected }.map { it.id },
                transactionType = TransactionType.EXPENSES
            ).collectLatest {
                it?.let { res ->
                    _viewState.value = _viewState.value.copy(
                        pieChartData = generatePieChartData(res.mapKeys { it.key.name }),
                        barChartData = generateBarChartData(res.mapKeys { it.key.name }),
                        currency = userCurrencyRepository.getPrimaryCurrency()
                    )
                }
            }
        }
    }

    fun onNavigateExpensesDetailPage() {
        routeNavigator.navigateTo(CategoryRoutes.Stat(_dateFilter.value))
    }

    private fun observeTagFilterChanged() {
        viewModelScope.launch {
            tagFilter.drop(1).collectLatest {
                refresh()
            }
        }
    }

    private fun observeDateFilterChanged() {
        viewModelScope.launch {
            dateFilter.drop(1).collectLatest {
                refresh()
            }
        }
    }

    fun onDateFilterChanged(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
    }

    fun onToggleTagDialog(isVisible: Boolean) {
        _viewState.update {
            it.copy(
                showTagFilterDialog = isVisible
            )
        }
    }

    fun onNavigateBudgetDetail() {
        routeNavigator.navigateTo(BudgetRoutes.BudgetDetail)
    }

    fun toggleChartType(chartType: ExpensesPieChartViewState.ChartType) {
        _viewState.update {
            it.copy(
                chartType = chartType
            )
        }
    }

    private fun loadTagOptions() {
        viewModelScope.launch {
            tagRepository.getAllTags().firstOrNull()?.let {
                _tagFilter.value = it.map {
                    TagFilterItem(
                        id = it.id,
                        name = it.name,
                        selected = false
                    )
                }
            }
        }
    }

    fun onSetTagFilter(tagFilter: List<TagFilterItem>) {
        _tagFilter.value = tagFilter
    }

    private fun generatePieChartData(data: Map<String, Long>): List<ExpensesPieChartData> {
        val res = mutableListOf<ExpensesPieChartData>()
        for ((rootCategory, amount) in data.entries) {
            if (amount > 0) {
                res.add(
                    ExpensesPieChartData(
                        data = amount.toDouble(), // Use max to eliminate negative value.
                        color = generateRandomColor(),
                        partName = rootCategory
                    )
                )
            }
        }
        return res
    }

    private suspend fun generateBarChartData(data: Map<String, Long>): ExpensesPieChartViewState.BarChartData {
        val sorted = data.toList().sortedByDescending { it.second }
        val res = mutableListOf<LineComponent>()
        res.add(
            LineComponent(
                color = generateRandomColor().toArgb(),
                shape = LineComponent.Shape.RoundedCorner
            )
        )

        val primaryCurrency = userCurrencyRepository.getPrimaryCurrency()

        // Convert minor units to major currency amounts using CurrencyFormatter
        val convertedAmounts = sorted.map { (category, minorAmount) ->
            val majorAmountString = currencyFormatter.formatWithoutSymbol(minorAmount, primaryCurrency)
            val majorAmount = majorAmountString.toDoubleOrNull() ?: 0.00
            category to majorAmount
        }

        return ExpensesPieChartViewState.BarChartData(
            currencyCode = primaryCurrency,
            categoryExpensesAmount = convertedAmounts,
        )
    }

    private fun generateRandomColor(): Color {
        val random = Random.Default
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        return Color(red, green, blue)
    }
}

internal data class TagFilterItem(
    val id: Int,
    val name: String,
    val selected: Boolean = false,
)

data class ExpensesPieChartViewState(
    val pieChartData: List<ExpensesPieChartData> = listOf(),
    val barChartData: BarChartData = BarChartData(),
    val showTagFilterDialog: Boolean = false,
    val chartType: ChartType = ChartType.Pie(),
    val currency: String = ""
) {

    sealed interface ChartType {
        val index: Int
        val name: String

        data class Pie(
            override val index: Int = 0,
            override val name: String = "PIE"
        ) : ChartType

        data class Bar(
            override val index: Int = 1,
            override val name: String = "BAR"
        ) : ChartType
    }

    data class BarChartData(
        val currencyCode: String = "",
        val categoryExpensesAmount: List<Pair<String, Double>> = listOf() // Change Long to Double
    )
}

data class ExpensesPieChartData(
    val data:Double,
    val color: Color,
    val partName:String
)

internal data class LineComponent(
    val color: Int,
    val shape: Shape
) {
    sealed class Shape {
        data object RoundedCorner : Shape()
    }
}