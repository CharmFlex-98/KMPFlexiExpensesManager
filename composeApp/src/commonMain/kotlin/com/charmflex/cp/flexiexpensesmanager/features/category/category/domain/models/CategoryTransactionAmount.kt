package com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.models

import androidx.room.ColumnInfo

internal data class CategoryTransactionAmount(
    val categoryId: Int,
    val categoryName: String,
    val parentCategoryId: Int,
    val expensesAmountInCent: Long
)