package com.charmflex.cp.flexiexpensesmanager.features.transactions.provider

import com.charmflex.cp.flexiexpensesmanager.core.domain.FEField
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named


internal const val TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT"
internal const val PRIMARY_TRANSACTION_AMOUNT = "PRIMARY_TRANSACTION_AMOUNT"
internal const val PRIMARY_CURRENCY_RATE = "PRIMARY_CURRENCY_RATE"
internal const val TRANSACTION_FROM_ACCOUNT = "TRANSACTION_FROM_ACC"
internal const val TRANSACTION_TO_ACCOUNT = "TRANSACTION_TO_ACC"
internal const val TRANSACTION_CURRENCY = "TRANSACTION_CURRENCY"

internal const val TRANSACTION_NAME = "TRANSACTION_NAME"
internal const val TRANSACTION_DATE = "TRANSACTION_DATE"
internal const val TRANSACTION_CATEGORY = "TRANSACTION_CAT"
internal const val TRANSACTION_TAG = "TRANSACTION_TAG"
internal const val TRANSACTION_SCHEDULER_PERIOD = "SCHEDULER_PERIOD"
internal const val TRANSACTION_UPDATE_ACCOUNT = "TRANSACTION_UPDATE_ACCOUNT"
internal const val TRANSACTION_UPDATE_ACCOUNT_TYPE = "TRANSACTION_UPDATE_ACCOUNT_TYPE"

internal class DefaultTransactionEditorContentProvider  constructor() :
    TransactionEditorContentProvider()

internal abstract class TransactionEditorContentProvider {
    open fun getContent(transactionType: TransactionType): List<FEField> {
        val res = when (transactionType) {
            TransactionType.EXPENSES -> expensesFields()
            TransactionType.INCOME -> incomeFields()
            TransactionType.TRANSFER -> transferFields()
            TransactionType.UPDATE_ACCOUNT -> updateFields()
        }.toMutableList()

        if (transactionType != TransactionType.UPDATE_ACCOUNT) {
            res.add(
                FEField(
                    id = TRANSACTION_DATE,
                    labelId = Res.string.new_expenses_date,
                    hintId = Res.string.new_expenses_date_hint,
                    type = FEField.FieldType.Callback
                )
            )
        }

        return res
    }

    private fun expensesFields(): List<FEField> {
        return listOf(
            FEField(
                id = TRANSACTION_NAME,
                labelId = Res.string.new_expenses_name,
                hintId = Res.string.new_expenses_name_hint,
                valueItem = FEField.Value(),
                type = FEField.FieldType.Text
            ),
            FEField(
                id = TRANSACTION_CURRENCY,
                labelId = Res.string.new_transaction_currency_label,
                hintId = Res.string.new_transaction_currency_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_FROM_ACCOUNT,
                labelId = Res.string.new_transaction_account,
                hintId = Res.string.new_expenses_account_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_AMOUNT,
                labelId = Res.string.new_expenses_amount,
                hintId = Res.string.new_expenses_name_amount_hint,
                type = FEField.FieldType.Currency
            ),
            FEField(
                id = TRANSACTION_CATEGORY,
                labelId = Res.string.generic_category,
                hintId = Res.string.generic_category_hint,
                type = FEField.FieldType.Callback
            )
        )
    }

    private fun incomeFields(): List<FEField> {
        return listOf(
            FEField(
                id = TRANSACTION_NAME,
                labelId = Res.string.new_expenses_name,
                hintId = Res.string.new_expenses_name_hint,
                valueItem = FEField.Value(),
                type = FEField.FieldType.Text
            ),
            FEField(
                id = TRANSACTION_TO_ACCOUNT,
                labelId = Res.string.new_transaction_account,
                hintId = Res.string.new_income_account_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_AMOUNT,
                labelId = Res.string.income_amount_label,
                hintId = Res.string.income_amount_hint,
                type = FEField.FieldType.Currency
            ),
            FEField(
                id = TRANSACTION_CATEGORY,
                labelId = Res.string.generic_category,
                hintId = Res.string.generic_category_hint,
                type = FEField.FieldType.Callback
            ),
        )
    }

    private fun transferFields(): List<FEField> {
        return listOf(
            FEField(
                id = TRANSACTION_NAME,
                labelId = Res.string.new_expenses_name,
                hintId = Res.string.new_expenses_name_hint,
                valueItem = FEField.Value(),
                type = FEField.FieldType.Text
            ),
            FEField(
                id = TRANSACTION_FROM_ACCOUNT,
                labelId = Res.string.new_transaction_from_account,
                hintId = Res.string.new_transaction_from_account_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_TO_ACCOUNT,
                labelId = Res.string.new_transaction_to_account,
                hintId = Res.string.new_transaction_to_account_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_AMOUNT,
                labelId = Res.string.transfer_amount_label,
                hintId = Res.string.transfer_amount_hint,
                type = FEField.FieldType.Currency
            )
        )
    }

    private fun updateFields(): List<FEField> {
        return listOf(
            FEField(
                id = TRANSACTION_UPDATE_ACCOUNT,
                labelId = Res.string.new_transaction_update_account_label,
                hintId = Res.string.new_transaction_update_account_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_UPDATE_ACCOUNT_TYPE,
                labelId = Res.string.new_transaction_update_account_type_label,
                hintId = Res.string.new_transaction_update_account_type_hint,
                type = FEField.FieldType.Callback
            ),
            FEField(
                id = TRANSACTION_AMOUNT,
                labelId = Res.string.new_expenses_amount,
                hintId = Res.string.new_expenses_name_amount_hint,
                type = FEField.FieldType.Currency
            ),
        )
    }

    fun additionalPrimaryCurrencyRateFields(): List<FEField> {
        return listOf(
            FEField(
                id = PRIMARY_TRANSACTION_AMOUNT,
                labelId = Res.string.primary_transaction_amount,
                hintId = Res.string.primary_transaction_amount_hint,
                type = FEField.FieldType.Number
            ),
            FEField(
                id = PRIMARY_CURRENCY_RATE,
                labelId = Res.string.primary_currency_rate_label,
                hintId = Res.string.primary_currency_rate_hint,
                type = FEField.FieldType.Number
            )
        )
    }
}