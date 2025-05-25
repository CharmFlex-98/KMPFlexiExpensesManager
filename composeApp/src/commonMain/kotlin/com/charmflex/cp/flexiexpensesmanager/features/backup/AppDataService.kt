package com.charmflex.cp.flexiexpensesmanager.features.backup

import com.charmflex.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository

internal interface AppDataService {
    suspend fun clearAppData(appDataClearServiceType: AppDataClearServiceType)
}

internal enum class AppDataClearServiceType {
    TRANSACTION_ONLY, ALL
}

internal class AppDataServiceImpl(
    private val transactionRepository: TransactionRepository,
    private val sharedPrefs: SharedPrefs,
//    private val appContext: Context,
    private val database: AppDatabase
) : AppDataService {
    override suspend fun clearAppData(appDataClearServiceType: AppDataClearServiceType) {
        when (appDataClearServiceType) {
            AppDataClearServiceType.ALL -> {
//                database.close()
//                appContext.deleteDatabase("FlexiExpensesManagerDB")
                sharedPrefs.clearAllData()
            }
            AppDataClearServiceType.TRANSACTION_ONLY -> {
                transactionRepository.deleteAllTransactions()
            }
        }
    }
}