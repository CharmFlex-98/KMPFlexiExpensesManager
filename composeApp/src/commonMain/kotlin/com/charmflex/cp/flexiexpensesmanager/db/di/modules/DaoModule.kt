package com.charmflex.flexiexpensesmanager.db.di.modules

import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountTransactionDao
import com.charmflex.flexiexpensesmanager.features.budget.data.daos.CategoryBudgetDao
import com.charmflex.flexiexpensesmanager.features.currency.data.daos.CurrencyDao
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionDao
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.flexiexpensesmanager.features.tag.data.daos.TagDao
import com.charmflex.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionTagDao
import dagger.Module
import dagger.Provides

@Module
internal interface DaoModule {

    companion object {
        @Provides
        fun accountDao(db: AppDatabase): AccountDao {
            return db.getAccountDao()
        }

        @Provides
        fun transactionDao(db: AppDatabase): TransactionDao {
            return db.getTransactionDao()
        }

        @Provides
        fun transactionCategoryDao(db: AppDatabase): TransactionCategoryDao {
            return db.getTransactionCategoryDao()
        }

        @Provides
        fun currencyDao(db: AppDatabase): CurrencyDao {
            return db.getCurrencyDao()
        }

        @Provides
        fun tagDao(db: AppDatabase): TagDao {
            return db.getTagDao()
        }

        @Provides
        fun transactionTagDao(db: AppDatabase): TransactionTagDao {
            return db.getTransactionTagDao()
        }

        @Provides
        fun accountTransactionDao(db: AppDatabase): AccountTransactionDao {
            return db.getAccountTransactionDao()
        }

        @Provides
        fun scheduledTransactionDao(db: AppDatabase): ScheduledTransactionDao {
            return db.getScheduledTransactionDao()
        }

        @Provides
        fun scheduledTransactionTagDao(db: AppDatabase): ScheduledTransactionTagDao {
            return db.getScheduledTransactionTagDao()
        }

        @Provides
        fun categoryBudgetDao(db: AppDatabase): CategoryBudgetDao {
            return db.getCategoryBudgetDao()
        }
    }
}