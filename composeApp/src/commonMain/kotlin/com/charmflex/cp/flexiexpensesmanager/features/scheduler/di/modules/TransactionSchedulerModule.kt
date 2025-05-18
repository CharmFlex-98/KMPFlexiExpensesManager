package com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules

import com.charmflex.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandler
import com.charmflex.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandlerImpl
import com.charmflex.flexiexpensesmanager.features.scheduler.data.mappers.ScheduledTransactionMapper
import com.charmflex.flexiexpensesmanager.features.scheduler.data.repositories.TransactionSchedulerRepositoryImpl
import com.charmflex.flexiexpensesmanager.features.scheduler.data.storage.TransactionSchedulerStorage
import com.charmflex.flexiexpensesmanager.features.scheduler.data.storage.TransactionSchedulerStorageImpl
import com.charmflex.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.ScheduledTransactionEditorContentProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val schedulerModule = module {
    singleOf(::TransactionSchedulerRepositoryImpl) { bind<TransactionSchedulerRepository>() }
    singleOf(::TransactionSchedulerStorageImpl) { bind<TransactionSchedulerStorage>() }
    singleOf(::ScheduledTransactionHandlerImpl) { bind<ScheduledTransactionHandler>() }
    singleOf(::ScheduledTransactionEditorContentProvider)
    factory { ScheduledTransactionMapper() }
    factory(named(TransactionEditorProvider.SCHEDULER)) { ScheduledTransactionEditorContentProvider() }
}



enum class TransactionEditorProvider {
    DEFAULT, SCHEDULER
}