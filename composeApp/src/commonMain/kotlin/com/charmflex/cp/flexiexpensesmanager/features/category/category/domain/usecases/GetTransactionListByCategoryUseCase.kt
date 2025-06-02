package com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.usecases

import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.core.annotation.Factory

@Factory
internal class GetTransactionListByCategoryUseCase constructor(
    private val categoryRepository: TransactionCategoryRepository,
) {
    suspend operator fun invoke(
        list: List<Transaction>,
        categoryId: Int,
        categoryName: String,
        transactionType: TransactionType,
    ): List<Transaction> {
        return categoryRepository.getCategoriesIncludeDeleted(transactionType.name)
            .firstOrNull()?.let {
                val rootCategories = it.items
                val categoryToRootMap = mutableMapOf<CategoryHolder, CategoryHolder>()
                rootCategories.forEach { root ->
                    buildCategoryToRootMapping(
                        rootBasicCategoryNode = root,
                        category = root,
                        categoryToRootMap
                    )
                }

                list
                    .filter { transaction ->
                        transaction.transactionCategory?.let {
                            categoryToRootMap[CategoryHolder(it.id, it.name)] == CategoryHolder(
                                categoryId,
                                categoryName
                            )
                        } ?: false
                    }

            } ?: listOf()
    }
}