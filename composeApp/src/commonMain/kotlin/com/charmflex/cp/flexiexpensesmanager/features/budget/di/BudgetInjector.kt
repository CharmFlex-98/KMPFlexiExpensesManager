package com.charmflex.cp.flexiexpensesmanager.features.budget.di

import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.setting.BudgetSettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.stats.BudgetDetailViewModel

internal interface BudgetInjector {
    fun budgetSettingViewModel(): BudgetSettingViewModel
    fun budgetDetailViewModel(): BudgetDetailViewModel
}