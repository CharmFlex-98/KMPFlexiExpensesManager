package com.charmflex.cp.flexiexpensesmanager.db.di.modules

import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.db.core.DatabaseBuilder
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountTransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.account.domain.model.AccountGroup
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.daos.CategoryBudgetDao
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.daos.CurrencyDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.daos.TagDao
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionTagDao
import org.koin.dsl.module

val daoModule = module {
    factory<AccountDao> { get<DatabaseBuilder.IDatabase>().getAccountDao() }
    factory<TransactionDao> { get<DatabaseBuilder.IDatabase>().getTransactionDao() }
    factory<TransactionCategoryDao> { get<DatabaseBuilder.IDatabase>().getTransactionCategoryDao() }
    factory<CurrencyDao> { get<DatabaseBuilder.IDatabase>().getCurrencyDao() }
    factory<TagDao> { get<DatabaseBuilder.IDatabase>().getTagDao() }
    factory<TransactionTagDao> { get<DatabaseBuilder.IDatabase>().getTransactionTagDao() }
    factory<AccountTransactionDao> { get<DatabaseBuilder.IDatabase>().getAccountTransactionDao() }
    factory<AccountDao> { get<DatabaseBuilder.IDatabase>().getAccountDao() }
    factory<ScheduledTransactionDao> { get<DatabaseBuilder.IDatabase>().getScheduledTransactionDao() }
    factory<ScheduledTransactionTagDao> { get<DatabaseBuilder.IDatabase>().getScheduledTransactionTagDao() }
    factory<CategoryBudgetDao> { get<DatabaseBuilder.IDatabase>().getCategoryBudgetDao() }
}