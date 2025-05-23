package com.charmflex.cp.flexiexpensesmanager.di

import com.charmflex.cp.flexiexpensesmanager.core.di.networkModule
import com.charmflex.cp.flexiexpensesmanager.features.account.di.modules.accountModule
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.modules.authModule
import com.charmflex.cp.flexiexpensesmanager.features.currency.di.modules.currencyModule
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules.schedulerModule
import com.charmflex.flexiexpensesmanager.features.backup.di.backupModule
import com.charmflex.flexiexpensesmanager.features.budget.di.modules.budgetModule
import com.charmflex.flexiexpensesmanager.features.transactions.di.modules.transactionModule
import org.koin.core.module.Module

fun commonModules(): List<Module> {
    return listOf(
        accountModule,
        authModule,
        backupModule,
        budgetModule,
        currencyModule,
        schedulerModule,
        transactionModule,
        networkModule
    )
}