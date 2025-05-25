package com.charmflex.cp.flexiexpensesmanager.features.transactions.di

import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.TransactionDetailViewModel

internal interface TransactionInjector {
    val transactionEditorViewModelFactory: TransactionEditorViewModel.Factory
    val transactionDetailViewModelFactory: TransactionDetailViewModel.Factory
}