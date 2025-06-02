package com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules

import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandler
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandlerImpl
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.mappers.ScheduledTransactionMapper
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.repositories.TransactionSchedulerRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.storage.TransactionSchedulerStorage
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.storage.TransactionSchedulerStorageImpl
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.ScheduledTransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.DefaultTransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TransactionEditorContentProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val schedulerModule = module {
    singleOf(::TransactionSchedulerRepositoryImpl) { bind<TransactionSchedulerRepository>() }
    singleOf(::TransactionSchedulerStorageImpl) { bind<TransactionSchedulerStorage>() }
    singleOf(::ScheduledTransactionHandlerImpl) { bind<ScheduledTransactionHandler>() }
    factory<TransactionEditorContentProvider>(named(TransactionEditorProvider.SCHEDULER)) { ScheduledTransactionEditorContentProvider() }
    factory { ScheduledTransactionMapper() }
}



enum class TransactionEditorProvider {
    DEFAULT, SCHEDULER
}