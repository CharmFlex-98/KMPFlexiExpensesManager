package com.charmflex.cp.flexiexpensesmanager.features.account.data.storage

import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs

private const val ACCOUNT_INFO_HIDE_KEY = "ACCOUNT_INFO_HIDE_KEY"

internal interface AccountStorage {
    suspend fun hideAccountInfo()

    suspend fun unHideAccountInfo()

    suspend fun getAccountInfoHidden(): Boolean
}

internal class AccountStorageImpl  constructor(
    private val sharedPrefs: SharedPrefs
) : AccountStorage {
    override suspend fun hideAccountInfo() {
        sharedPrefs.setBoolean(ACCOUNT_INFO_HIDE_KEY, true)
    }

    override suspend fun unHideAccountInfo() {
        sharedPrefs.setBoolean(ACCOUNT_INFO_HIDE_KEY, false)
    }

    override suspend fun getAccountInfoHidden(): Boolean {
        return sharedPrefs.getBoolean(ACCOUNT_INFO_HIDE_KEY, false)
    }
}

