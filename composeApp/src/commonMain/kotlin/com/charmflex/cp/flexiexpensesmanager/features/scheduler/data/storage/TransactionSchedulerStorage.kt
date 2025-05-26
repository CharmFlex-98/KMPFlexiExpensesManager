package com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.storage

import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs

private const val TRANSACTION_SCHEDULER_LATEST_ID = "TRANSACTION_SCHEDULER_LATEST_ID"
internal interface TransactionSchedulerStorage {
    fun setLatestId(id: Int)

    fun getLatestId(): Int


}

internal class TransactionSchedulerStorageImpl constructor(
    private val sharedPrefs: SharedPrefs
) : TransactionSchedulerStorage {
    override fun setLatestId(id: Int) {
        sharedPrefs.setInt(TRANSACTION_SCHEDULER_LATEST_ID, id)
    }

    override fun getLatestId(): Int {
        return sharedPrefs.getInt(TRANSACTION_SCHEDULER_LATEST_ID, -1)
    }
}