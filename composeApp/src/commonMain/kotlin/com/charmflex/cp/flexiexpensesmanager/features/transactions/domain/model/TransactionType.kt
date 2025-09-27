package com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_expenses
import kotlinproject.composeapp.generated.resources.generic_income
import kotlinproject.composeapp.generated.resources.generic_transfer
import kotlinproject.composeapp.generated.resources.generic_update_account
import org.jetbrains.compose.resources.StringResource

// TODO: Need to handle locale
internal enum class TransactionType() {
    EXPENSES, INCOME, TRANSFER, UPDATE_ACCOUNT;

    companion object {
        fun getStringRes(transactionType: TransactionType): StringResource {
            return when (transactionType) {
                TransactionType.EXPENSES -> Res.string.generic_expenses
                TransactionType.INCOME -> Res.string.generic_income
                TransactionType.TRANSFER -> Res.string.generic_transfer
                TransactionType.UPDATE_ACCOUNT -> Res.string.generic_update_account
            }
        }
    }
}