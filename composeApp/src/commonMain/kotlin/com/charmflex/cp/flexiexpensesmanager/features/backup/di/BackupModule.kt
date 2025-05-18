package com.charmflex.flexiexpensesmanager.features.backup.di

import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataService
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val backupModule = module {
    singleOf(::AppDataServiceImpl) { bind<AppDataService>() }
}