package com.charmflex.cp.flexiexpensesmanager.features.budget.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("budget_month_year", unique = true)
    ]
)
internal data class MonthlyCategoryBudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("budget_month_year")
    val budgetMonthYear: String,
    @ColumnInfo("custom_minor_unit_budget")
    val customMinorUnitBudget: Long,
    @ColumnInfo("category_budget_id")
    val categoryBudgetId: Int,
)