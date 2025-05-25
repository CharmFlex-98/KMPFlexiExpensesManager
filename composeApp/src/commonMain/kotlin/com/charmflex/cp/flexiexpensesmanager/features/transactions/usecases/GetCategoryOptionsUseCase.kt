package com.charmflex.cp.flexiexpensesmanager.features.transactions.usecases

import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository

internal class GetCategoryOptionsUseCase constructor(
    private val transactionCategoryRepository: TransactionCategoryRepository
) {
//    suspend operator fun invoke(transactionTypeCode: String): List<CategorySelectionItem> {
//        return transactionCategoryRepository.getAllCategories(transactionTypeCode).items.map {
//            CategorySelectionItem(
//                id = it.categoryId.toString(),
//                title = it.categoryName
//            )
//        }
//    }
}