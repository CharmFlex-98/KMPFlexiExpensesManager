package com.charmflex.cp.flexiexpensesmanager.db

import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.repositories.CategoryAmountNode
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

internal enum class AccountGroupEnum(
    val value: String
) {
    BANK_ACCOUNT(AccountGroupName.BANK_ACCOUNT),
    CASH(AccountGroupName.CASH),
    CREDIT_CARD(AccountGroupName.CREDIT_CARD),
    DEBIT_CARD(AccountGroupName.DEBIT_CARD),
    INSURANCE(AccountGroupName.INSURANCE),
    INVESTMENT(AccountGroupName.INVESTMENT),
    LOAN(AccountGroupName.LOAN),
    PREPAID(AccountGroupName.PREPAID),
    SAVING(AccountGroupName.SAVING),
    OTHERS(AccountGroupName.OTHERS);

    companion object {
        fun from(value: String): AccountGroupEnum {
            return when (value) {
                AccountGroupName.BANK_ACCOUNT -> BANK_ACCOUNT
                AccountGroupName.CASH -> CASH
                AccountGroupName.CREDIT_CARD -> CREDIT_CARD
                AccountGroupName.DEBIT_CARD -> DEBIT_CARD
                AccountGroupName.INSURANCE -> INSURANCE
                AccountGroupName.INVESTMENT -> INVESTMENT
                AccountGroupName.LOAN -> LOAN
                AccountGroupName.PREPAID -> PREPAID
                AccountGroupName.SAVING -> SAVING
                AccountGroupName.OTHERS -> OTHERS
                else -> OTHERS
            }
        }
    }
}

internal object AccountGroupName {
    const val BANK_ACCOUNT = "Bank Account"
    const val CASH = "Cash"
    const val CREDIT_CARD = "Credit Card"
    const val DEBIT_CARD = "Debit Card"
    const val INSURANCE = "Insurance"
    const val INVESTMENT = "Investment"
    const val LOAN = "Loan"
    const val PREPAID = "Prepaid"
    const val SAVING = "Saving"
    const val OTHERS = "Others"
}


internal fun getAccountGroupNameRes(accountGroupName: String): StringResource {
    val accountGroupEnum = AccountGroupEnum.from(accountGroupName)
    return when (accountGroupEnum) {
        AccountGroupEnum.BANK_ACCOUNT -> Res.string.account_group_bank_account
        AccountGroupEnum.CASH -> Res.string.account_group_cash
        AccountGroupEnum.CREDIT_CARD -> Res.string.account_group_credit_card
        AccountGroupEnum.DEBIT_CARD -> Res.string.account_group_debit_card
        AccountGroupEnum.INSURANCE -> Res.string.account_group_insurance
        AccountGroupEnum.INVESTMENT -> Res.string.account_group_investment
        AccountGroupEnum.LOAN -> Res.string.account_group_loan
        AccountGroupEnum.PREPAID -> Res.string.account_group_prepaid
        AccountGroupEnum.SAVING -> Res.string.account_group_saving
        AccountGroupEnum.OTHERS -> Res.string.account_group_others
    }
}