package com.charmflex.flexiexpensesmanager.features.transactions.domain.model

import com.charmflex.cp.flexiexpensesmanager.features.account.domain.model.AccountGroup
import com.charmflex.flexiexpensesmanager.features.tag.domain.model.Tag
import kotlinx.serialization.Serializable

internal data class Transaction(
    val transactionId: Long,
    val transactionName: String,
    val transactionAccountFrom: AccountGroup.Account?,
    val transactionAccountTo: AccountGroup.Account?,
    val transactionTypeCode: String,
    val minorUnitAmount: Long,
    val currency: String,
    val accountMinorUnitAmount: Long,
    val primaryMinorUnitAmount: Long,
    val transactionDate: String,
    val transactionCategory: TransactionCategory?,
    val tags: List<Tag>
) {

    @Serializable
    data class TransactionCategory(
        val id: Int,
        val name: String
    )
}