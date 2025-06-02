package com.charmflex.cp.flexiexpensesmanager.features.budget.di.modules

import com.charmflex.cp.flexiexpensesmanager.features.budget.data.repositories.CategoryBudgetRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.budget.domain.repositories.CategoryBudgetRepository
import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.setting.BudgetSettingViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val budgetModule = module {
    singleOf(::CategoryBudgetRepositoryImpl) { bind<CategoryBudgetRepository>() }
}