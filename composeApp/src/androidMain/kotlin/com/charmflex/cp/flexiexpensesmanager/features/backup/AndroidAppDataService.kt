package com.charmflex.cp.flexiexpensesmanager.features.backup

import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.db.core.DatabaseBuilder
import com.charmflex.cp.flexiexpensesmanager.di.ActivityProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository

internal class AndroidAppDataService(
    private val transactionRepository: TransactionRepository,
    private val sharedPrefs: SharedPrefs,
    private val appContext: Context,
    private val activityProvider: ActivityProvider
) : AppDataService {
    override suspend fun clearAppData(appDataClearServiceType: AppDataClearServiceType) {
        when (appDataClearServiceType) {
            AppDataClearServiceType.ALL -> {
                appContext.deleteDatabase("FlexiExpensesManagerDB")
                sharedPrefs.clearAllData()
                activityProvider.currentActivity?.finish()
            }
            AppDataClearServiceType.TRANSACTION_ONLY -> {
                transactionRepository.deleteAllTransactions()
            }
        }
    }
}