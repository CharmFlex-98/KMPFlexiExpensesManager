package com.charmflex.cp.flexiexpensesmanager.features.account.domain.model

import kotlinx.serialization.Serializable


internal data class AccountGroup(
    val accountGroupId: Int,
    val accountGroupName: String,
    val accounts: List<Account>
) {
    @Serializable
    data class Account(
        val accountId: Int,
        val accountName: String,
        val currency: String,
        val isDeleted: Boolean
    )
}