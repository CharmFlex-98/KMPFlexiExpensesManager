package com.charmflex.cp.flexiexpensesmanager.features.backup

import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.db.core.DatabaseBuilder
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository

internal interface AppDataService {
    suspend fun clearAppData(appDataClearServiceType: AppDataClearServiceType)
}

internal enum class AppDataClearServiceType {
    TRANSACTION_ONLY, ALL
}