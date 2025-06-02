package com.charmflex.cp.flexiexpensesmanager.features.account.di.modules
import AccountHiderService
import AccountHiderServiceImpl
import AccountRepository
import com.charmflex.cp.flexiexpensesmanager.features.account.data.repositories.AccountRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.account.data.storage.AccountStorage
import com.charmflex.cp.flexiexpensesmanager.features.account.data.storage.AccountStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val accountModule = module {
    singleOf(::AccountStorageImpl) { bind<AccountStorage>() }
    singleOf(::AccountStorageImpl) { bind<AccountStorage>() }
    singleOf(::AccountRepositoryImpl) { bind<AccountRepository>() }
    singleOf(::AccountHiderServiceImpl) { bind<AccountHiderService>() }
}