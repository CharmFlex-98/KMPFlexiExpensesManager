package com.charmflex.cp.flexiexpensesmanager.features.category.category.di

import com.charmflex.flexiexpensesmanager.features.category.category.ui.CategoryEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.detail.CategoryDetailViewModel
import com.charmflex.flexiexpensesmanager.features.category.category.ui.stat.CategoryStatViewModel

internal interface CategoryInjector {
    val categoryEditorViewModel: CategoryEditorViewModel
    val categoryStatViewModel: CategoryStatViewModel
    val categoryDetailViewModelFactory: CategoryDetailViewModel.Factory

}