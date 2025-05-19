package com.charmflex.cp.flexiexpensesmanager.features.budget.di

import com.charmflex.flexiexpensesmanager.features.budget.ui.setting.BudgetSettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.stats.BudgetDetailViewModel

internal interface BudgetInjector {
    val budgetSettingViewModel: BudgetSettingViewModel
    val budgetDetailViewModel: BudgetDetailViewModel
}