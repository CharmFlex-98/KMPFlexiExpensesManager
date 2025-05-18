package com.charmflex.cp.flexiexpensesmanager.features.currency.di.modules

import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatterImpl
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.local.CurrencyKeyStorage
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.local.CurrencyKeyStorageImpl
import com.charmflex.flexiexpensesmanager.features.currency.data.repositories.CurrencyRepositoryImpl
import com.charmflex.flexiexpensesmanager.features.currency.data.repositories.UserCurrencyRepositoryImpl
import com.charmflex.flexiexpensesmanager.features.currency.domain.repositories.CurrencyRepository
import com.charmflex.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val currencyModule = module {
    singleOf(::CurrencyRepositoryImpl) { bind<CurrencyRepository>() }
    singleOf(::UserCurrencyRepositoryImpl) { bind<UserCurrencyRepository>() }
    singleOf(::CurrencyFormatterImpl) { bind<CurrencyFormatter>() }
    singleOf(::CurrencyKeyStorageImpl) { bind<CurrencyKeyStorage>() }
}