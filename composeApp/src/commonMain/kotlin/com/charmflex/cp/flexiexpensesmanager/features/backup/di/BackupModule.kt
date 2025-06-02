package com.charmflex.cp.flexiexpensesmanager.features.backup.di

import com.charmflex.cp.flexiexpensesmanager.core.di.DispatcherType
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataService
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataServiceImpl
import com.charmflex.cp.flexiexpensesmanager.features.backup.checker.ImportDataChecker
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


val backupModule = module {
    singleOf(::AppDataServiceImpl) { bind<AppDataService>() }
    factory { ImportDataChecker(
        get(),
        get(),
        get(),
        get(named(DispatcherType.IO))
    ) }
}