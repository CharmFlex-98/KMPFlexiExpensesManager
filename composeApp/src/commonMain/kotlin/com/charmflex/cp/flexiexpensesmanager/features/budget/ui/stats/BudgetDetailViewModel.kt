package com.charmflex.cp.flexiexpensesmanager.features.budget.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.features.budget.domain.usecases.GetAdjustedCategoryBudgetInfoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class BudgetDetailViewModel constructor(
    private val getAdjustedCategoryBudgetInfoUseCase: GetAdjustedCategoryBudgetInfoUseCase,
    private val mapper: CategoryBudgetExpandableSectionMapper
) : ViewModel() {
    private val _viewState = MutableStateFlow(BudgetStatViewState())
    val viewState = _viewState.asStateFlow()

    private val _dateFilter: MutableStateFlow<DateFilter> = MutableStateFlow(DateFilter.Monthly(0))
    val dateFilter = _dateFilter.asStateFlow()

    init {
        observeDateFilterChanged()
    }

    private fun observeDateFilterChanged() {
        viewModelScope.launch {
            dateFilter.collectLatest {
                val res = getAdjustedCategoryBudgetInfoUseCase(_dateFilter.value).first()
                _viewState.update {
                    it.copy(
                        budgets = mapper.map(res)
                    )
                }
            }
        }
    }

    fun onDateFilterChanged(dateFilter: DateFilter) {
        _dateFilter.value = dateFilter
    }

    fun onToggleExpandable(budgetItem: BudgetStatViewState.CategoryBudgetItem) {
        val expandedCatIds = _viewState.value.expandedCategoryIds

        _viewState.update {
            it.copy(
                expandedCategoryIds = if (expandedCatIds.contains(budgetItem.categoryId)) expandedCatIds - budgetItem.categoryId
                else expandedCatIds + budgetItem.categoryId
            )
        }
    }
}


internal data class BudgetStatViewState(
    val budgets: List<CategoryBudgetExpandableSection> = emptyList(),
    // Show all root items with budget which is of 0 parent id
    val expandedCategoryIds: Set<Int> = setOf(0)
) {

    val onScreenBudgetSections: List<CategoryBudgetExpandableSection>
        get() {
            return budgets.map {
                it.copy(
                    contents = it.contents.filter {
                        expandedCategoryIds.containsAll(it.parentCategoryIds)
                    }
                )
            }
        }
    fun isItemExpanded(item: CategoryBudgetItem): Boolean {
        return item.expandable && item.categoryId in expandedCategoryIds
    }

    data class CategoryBudgetExpandableSection(
        val contents: List<CategoryBudgetItem>
    )

    data class CategoryBudgetItem(
        val categoryId: Int,
        val categoryName: String,
        val parentCategoryIds: Set<Int>,
        val budget: String,
        val expensesAmount: String,
        val expensesBudgetRatio: Float,
        val level: Level,
        val expandable: Boolean
    ) {
        enum class Level {
            FIRST, SECOND, THIRD
        }
    }
}