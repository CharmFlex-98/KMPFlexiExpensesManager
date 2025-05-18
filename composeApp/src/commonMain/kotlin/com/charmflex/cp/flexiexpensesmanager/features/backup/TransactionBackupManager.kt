package com.charmflex.cp.flexiexpensesmanager.features.backup
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

internal interface TransactionBackupManager {
    val progress: Flow<Float>

    suspend fun read(fileName: String): List<TransactionBackupData>

    suspend fun write(fileName: String)

    suspend fun share(fileName: String)
}
internal data class TransactionBackupData(
    val transactionName: String,
    val accountFrom: String?,
    val accountTo: String?,
    val transactionType: String,
    val currency: String,
    val accountAmount: Double,
    val primaryAmount: Double,
    val amount: Double,
    val date: LocalDate,
    val categoryColumns: List<String>,
    val tags: List<String>
)

