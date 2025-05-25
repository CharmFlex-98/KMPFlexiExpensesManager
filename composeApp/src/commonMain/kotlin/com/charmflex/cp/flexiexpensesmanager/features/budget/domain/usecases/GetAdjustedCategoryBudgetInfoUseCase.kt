package com.charmflex.cp.flexiexpensesmanager.features.budget.domain.usecases

import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.getEndDate
import com.charmflex.cp.flexiexpensesmanager.core.utils.getStartDate
import com.charmflex.cp.flexiexpensesmanager.features.budget.domain.models.AdjustedCategoryBudgetNode
import com.charmflex.cp.flexiexpensesmanager.features.budget.domain.repositories.CategoryBudgetRepository
import kotlinx.coroutines.flow.Flow

internal class GetAdjustedCategoryBudgetInfoUseCase constructor(
    private val categoryBudgetRepository: CategoryBudgetRepository
) {

    operator fun invoke(
        dateFilter: DateFilter
    ): Flow<List<AdjustedCategoryBudgetNode>> {
        val startDate = dateFilter.getStartDate()
        val endDate = dateFilter.getEndDate()

        // TODO: Better handling here
        return categoryBudgetRepository.getMonthlyCategoryBudgetInfo(startDate!!, endDate!!)
    }
}