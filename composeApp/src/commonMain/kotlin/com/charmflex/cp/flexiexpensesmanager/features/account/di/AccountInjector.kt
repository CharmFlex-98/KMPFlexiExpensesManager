package com.charmflex.cp.flexiexpensesmanager.features.account.di

import com.charmflex.cp.flexiexpensesmanager.features.account.ui.AccountEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.account_detail.AccountDetailViewModel
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.account_detail.AccountDetailViewModelFactory

internal interface AccountInjector {
    val accountEditorViewModel: AccountEditorViewModel
    val accountDetailViewModelFactory: AccountDetailViewModelFactory
}