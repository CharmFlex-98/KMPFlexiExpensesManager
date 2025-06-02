package com.charmflex.cp.flexiexpensesmanager.features.backup.di.modules

import com.charmflex.cp.flexiexpensesmanager.features.backup.AndroidTransactionBackupManager
import com.charmflex.cp.flexiexpensesmanager.features.backup.TransactionBackupManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val androidBackupModules = module {
    singleOf(::AndroidTransactionBackupManager) { bind<TransactionBackupManager>() }
}