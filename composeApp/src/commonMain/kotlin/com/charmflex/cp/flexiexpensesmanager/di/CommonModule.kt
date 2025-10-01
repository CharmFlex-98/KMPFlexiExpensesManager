package com.charmflex.cp.flexiexpensesmanager.di

import com.charmflex.cp.flexiexpensesmanager.core.di.mainModule
import com.charmflex.cp.flexiexpensesmanager.db.di.modules.daoModule
import com.charmflex.cp.flexiexpensesmanager.features.account.di.modules.accountModule
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.modules.authModule
import com.charmflex.cp.flexiexpensesmanager.features.currency.di.modules.currencyModule
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules.schedulerModule
import com.charmflex.cp.flexiexpensesmanager.features.backup.di.backupModule
import com.charmflex.cp.flexiexpensesmanager.features.budget.di.modules.budgetModule
import com.charmflex.cp.flexiexpensesmanager.features.session.di.module.sessionModule
import com.charmflex.cp.flexiexpensesmanager.features.transactions.di.modules.transactionModule
import org.koin.core.module.Module
import org.koin.ksp.generated.module


fun commonModules(): List<Module> {
    return listOf(
        mainModule,
        accountModule,
        authModule,
        backupModule,
        budgetModule,
        currencyModule,
        schedulerModule,
        transactionModule,
        daoModule,
        sessionModule,
        AppModule().module
    )
}