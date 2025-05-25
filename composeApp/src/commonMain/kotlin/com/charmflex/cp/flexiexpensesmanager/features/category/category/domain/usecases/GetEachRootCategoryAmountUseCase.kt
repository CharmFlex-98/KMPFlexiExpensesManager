package com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.usecases

import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.getEndDate
import com.charmflex.cp.flexiexpensesmanager.core.utils.getStartDate
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.models.TransactionCategories
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

internal class GetEachRootCategoryAmountUseCase constructor(
    private val categoryRepository: TransactionCategoryRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(tagFilter: List<Int> = listOf(), dateFilter: DateFilter? = null, transactionType: TransactionType): Flow<Map<CategoryHolder, Long>?> {
        val startDate = dateFilter.getStartDate()
        val endDate = dateFilter.getEndDate()

        return categoryRepository.getAllCategoryTransactionAmount(startDate, endDate, transactionType.name).transformLatest { nodeList ->
            val res: MutableMap<CategoryHolder, Long> = mutableMapOf()
            nodeList.forEach {
                if (it.adjustedExpensesAmountInMinorUnit != 0L) {
                    res[CategoryHolder(it.categoryId, it.categoryName)] = it.adjustedExpensesAmountInMinorUnit
                }
            }
            emit(res)
        }
    }
}

internal data class CategoryHolder(
    val id: Int,
    val name: String,
)

internal fun buildCategoryToRootMapping(rootBasicCategoryNode: TransactionCategories.BasicCategoryNode, category: TransactionCategories.BasicCategoryNode, currentMap: MutableMap<CategoryHolder, CategoryHolder>): Map<CategoryHolder, CategoryHolder> {
    currentMap[CategoryHolder(category.categoryId, category.categoryName)] = CategoryHolder(rootBasicCategoryNode.categoryId, rootBasicCategoryNode.categoryName)
    category.children.forEach {
        buildCategoryToRootMapping(rootBasicCategoryNode, it, currentMap)
    }

    return currentMap
}