package com.charmflex.cp.flexiexpensesmanager.db.core

import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountTransactionDao
import com.charmflex.flexiexpensesmanager.features.budget.data.daos.CategoryBudgetDao
import com.charmflex.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.flexiexpensesmanager.features.currency.data.daos.CurrencyDao
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionDao
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.flexiexpensesmanager.features.tag.data.daos.TagDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionTagDao


internal interface DatabaseBuilder {
    interface IDatabase {
        fun getAccountDao(): AccountDao
        fun getTransactionDao(): TransactionDao
        fun getTransactionCategoryDao(): TransactionCategoryDao
        fun getCurrencyDao(): CurrencyDao
        fun getTagDao(): TagDao
        fun getTransactionTagDao(): TransactionTagDao
        fun getAccountTransactionDao(): AccountTransactionDao
        fun getScheduledTransactionDao(): ScheduledTransactionDao
        fun getScheduledTransactionTagDao(): ScheduledTransactionTagDao
        fun getCategoryBudgetDao(): CategoryBudgetDao
    }

    fun build(): IDatabase
}


