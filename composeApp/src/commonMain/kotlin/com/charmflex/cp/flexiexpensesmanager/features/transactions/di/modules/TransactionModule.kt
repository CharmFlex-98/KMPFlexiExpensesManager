package com.charmflex.cp.flexiexpensesmanager.features.transactions.di.modules

import com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules.TransactionEditorProvider
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.repositories.TransactionCategoryRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.repositories.TagRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.repositories.TagRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.repositories.TransactionRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.DefaultTransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorViewModelFactory
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionModule = module {
    factory(named(TransactionEditorProvider.DEFAULT)) {
        DefaultTransactionEditorContentProvider()
    }.bind(TransactionEditorContentProvider::class)
    singleOf(::TagRepositoryImpl) { bind<TagRepository>() }
    singleOf(::TransactionRepositoryImpl) { bind<TransactionRepository>() }
    singleOf(::TransactionCategoryRepositoryImpl) { bind<TransactionCategoryRepository>() }
    factory { TransactionEditorViewModelFactory(
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(named(TransactionEditorProvider.DEFAULT)),
    ) }
}