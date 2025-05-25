package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.mapper

import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DATE_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.MONTH_YEAR_PATTERN
import com.charmflex.flexiexpensesmanager.core.utils.Mapper
import com.charmflex.cp.flexiexpensesmanager.core.utils.toLocalDate
import com.charmflex.cp.flexiexpensesmanager.core.utils.toStringWithPattern
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.TransactionHistoryHeader
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.TransactionHistoryItem
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.TransactionHistorySection
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.ic_spend
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

internal class TransactionHistoryMapper constructor(
    private val currencyFormatter: CurrencyFormatter
) : Mapper<List<Transaction>, List<TransactionHistoryItem>> {
    override fun map(from: List<Transaction>): List<TransactionHistoryItem> {
        val res = mutableListOf<TransactionHistoryItem>()
        val sectionSet = mutableSetOf<String>()
        from.groupBy {
            val date = it.transactionDate
            Pair(date.toMonthYearFormat(), date)
        }.map { (pair , history) ->
            val monthYear = pair.first
            val date = pair.second
            val dateIncluded = sectionSet.contains(monthYear).also {
                if (!it) sectionSet.add(monthYear)
            }

            res.add(
                TransactionHistoryHeader(
                    dateKey = date,
                    title = if (dateIncluded) null else monthYear,
                    subtitle = date
                )
            )

            res.add(
                TransactionHistorySection(
                    dateKey = date,
                    items = history.map {
                        TransactionHistorySection.SectionItem(
                            id = it.transactionId,
                            name = it.transactionName,
                            amount = currencyFormatter.format(it.minorUnitAmount, it.currency),
                            type = it.transactionTypeCode,
                            category = it.transactionCategory?.name ?: "",
                            iconResId = it.transactionTypeCode.getTransactionIconResId()
                        )
                    }
                )
            )
        }

        return res
    }
}

private fun String.getTransactionIconResId(): DrawableResource {
    return when (this) {
        TransactionType.TRANSFER.name -> Res.drawable.ic_transfer_icon
        TransactionType.INCOME.name -> Res.drawable.ic_income
        else -> Res.drawable.ic_spend
    }
}

private fun String.toMonthYearFormat(): String {
    return this.toLocalDate(DATE_ONLY_DEFAULT_PATTERN).toStringWithPattern(MONTH_YEAR_PATTERN)
}