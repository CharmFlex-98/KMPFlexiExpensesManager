package com.charmflex.cp.flexiexpensesmanager.features.budget.domain.models

internal data class CategoryBudgetDomainModel(
    val id: Int,
    val categoryName: String,
    val budgetInCent: Long
)