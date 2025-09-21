package com.charmflex.cp.flexiexpensesmanager.features.backup.di

import com.charmflex.cp.flexiexpensesmanager.core.di.DispatcherType
import com.charmflex.cp.flexiexpensesmanager.features.backup.checker.ImportDataChecker
import org.koin.core.qualifier.named
import org.koin.dsl.module


val backupModule = module {
    factory { ImportDataChecker(
        get(),
        get(),
        get(),
        get(named(DispatcherType.IO))
    ) }
}