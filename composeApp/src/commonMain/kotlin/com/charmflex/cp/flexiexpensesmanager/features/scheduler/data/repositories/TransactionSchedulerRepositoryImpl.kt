package com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.repositories

import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.entities.ScheduledTransactionEntity
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.mappers.ScheduledTransactionMapper
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.SchedulerPeriod
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.ScheduledTransaction
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TransactionSchedulerFile = "transaction_scheduler_file.txt"

internal class TransactionSchedulerRepositoryImpl constructor(
    private val scheduledTransactionTagDao: ScheduledTransactionTagDao,
    private val mapper: ScheduledTransactionMapper
) : TransactionSchedulerRepository {
    override suspend fun addScheduler(
        name: String,
        fromAccountId: Int?,
        toAccountId: Int?,
        categoryId: Int?,
        transactionType: TransactionType,
        amount: Long,
        startUpdateDate: String,
        currency: String,
        accountMinorUnitAmount: Long,
        primaryMinorUnitAmount: Long,
        tagIds: List<Int>,
        schedulerPeriod: SchedulerPeriod
    ) : Long {
        // Debug logging
        println("TransactionSchedulerRepositoryImpl.addScheduler - fromAccountId: $fromAccountId, toAccountId: $toAccountId")
        
        val scheduledTransaction = ScheduledTransactionEntity(
            transactionName = name,
            accountFromId = fromAccountId,
            accountToId = toAccountId,
            categoryId = categoryId,
            transactionType = transactionType.name,
            minorUnitAmount = amount,
            startUpdateDate = startUpdateDate,
            nextUpdateDate = startUpdateDate,
            currency = currency,
            accountMinorUnitAmount = accountMinorUnitAmount,
            primaryMinorUnitAmount = primaryMinorUnitAmount,
            schedulerPeriod = schedulerPeriod.name
        )
        
        println("Entity before insert - accountFromId: ${scheduledTransaction.accountFromId}")
        val insertedId = scheduledTransactionTagDao.insertScheduledTransactionAndTags(scheduledTransaction, tagIds)
        
        // Verify what was actually inserted by querying it back
        val insertedRecord = scheduledTransactionTagDao.getScheduledTransactionById(insertedId)
        println("Verification after insert - ID: $insertedId, accountFromId: ${insertedRecord?.scheduledAccountFromId}")
        
        return insertedId
    }

    override suspend fun updateScheduler(
        id: Long,
        name: String,
        fromAccountId: Int?,
        toAccountId: Int?,
        categoryId: Int?,
        transactionType: TransactionType,
        amount: Long,
        startUpdateDate: String,
        nextUpdateDate: String,
        currency: String,
        accountMinorUnitAmount: Long,
        primaryMinorUnitAmount: Long,
        tagIds: List<Int>,
        schedulerPeriod: SchedulerPeriod
    ) {
        val scheduledTransaction = ScheduledTransactionEntity(
            id = id,
            transactionName = name,
            accountFromId = fromAccountId,
            accountToId = toAccountId,
            categoryId = categoryId,
            transactionType = transactionType.name,
            minorUnitAmount = amount,
            startUpdateDate = startUpdateDate,
            nextUpdateDate = nextUpdateDate,
            currency = currency,
            accountMinorUnitAmount = accountMinorUnitAmount,
            primaryMinorUnitAmount = primaryMinorUnitAmount,
            schedulerPeriod = schedulerPeriod.name
        )
        scheduledTransactionTagDao.updateScheduledTransactionAndTags(scheduledTransaction, tagIds)
    }

    override fun getAllTransactionSchedulers(): Flow<List<ScheduledTransaction>> {
        val res = scheduledTransactionTagDao.getAllTransactionScheduler()
        return res.map {
            it.map { item ->
                mapper.map(item)
            }
        }

    }

    override suspend fun getTransactionSchedulerById(id: Long): ScheduledTransaction? {
        return scheduledTransactionTagDao.getScheduledTransactionById(id)?.let {
            println("mapper in action: " + it.scheduledAccountFromId)
            mapper.map(it)
        }
    }

    override suspend fun removeSchedulerById(id: Int) {
        scheduledTransactionTagDao.deleteSchedulerById(id.toLong())
    }
}

private suspend fun <T> catchException(default: T?, operation: suspend () -> T): T? {
    return try {
        operation()
    } catch (e: Exception) {
        default
    }
}