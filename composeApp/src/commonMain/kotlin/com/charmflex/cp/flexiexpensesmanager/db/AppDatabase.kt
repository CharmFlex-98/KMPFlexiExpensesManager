package com.charmflex.cp.flexiexpensesmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import com.charmflex.cp.flexiexpensesmanager.db.core.DatabaseBuilder
import com.charmflex.flexiexpensesmanager.db.AccountGroupName
import com.charmflex.flexiexpensesmanager.db.migration.Migration_2_3
import com.charmflex.flexiexpensesmanager.db.migration.Migration_5_6
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountTransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.entities.AccountEntity
import com.charmflex.cp.flexiexpensesmanager.features.account.data.entities.AccountGroupEntity
import com.charmflex.flexiexpensesmanager.features.budget.data.daos.CategoryBudgetDao
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.entities.CategoryBudgetEntity
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.entities.MonthlyCategoryBudgetEntity
import com.charmflex.flexiexpensesmanager.features.currency.data.daos.CurrencyDao
import com.charmflex.flexiexpensesmanager.features.currency.data.models.UserCurrencyRateEntity
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionDao
import com.charmflex.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.entities.ScheduledTransactionEntity
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.entities.ScheduledTransactionTagEntity
import com.charmflex.flexiexpensesmanager.features.tag.data.daos.TagDao
import com.charmflex.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.flexiexpensesmanager.features.transactions.data.daos.TransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.entities.TagEntity
import com.charmflex.flexiexpensesmanager.features.transactions.data.daos.TransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.entities.TransactionCategoryEntity
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.entities.CurrencyMetaDataEntity
import com.charmflex.flexiexpensesmanager.features.currency.data.utils.SQLQueryBuilder
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.entities.TransactionEntity
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.entities.TransactionTagEntity
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.entities.TransactionTypeEntity
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Database(
    entities = [
        AccountEntity::class,
        AccountGroupEntity::class,
        TagEntity::class,
        TransactionCategoryEntity::class,
        TransactionEntity::class,
        TransactionTagEntity::class,
        TransactionTypeEntity::class,
        UserCurrencyRateEntity::class,
        ScheduledTransactionEntity::class,
        ScheduledTransactionTagEntity::class,
        CategoryBudgetEntity::class,
        MonthlyCategoryBudgetEntity::class,
        CurrencyMetaDataEntity::class
    ],
    version = 1,
    exportSchema = true
)
internal abstract class AppDatabase : RoomDatabase(), DatabaseBuilder.IDatabase {
    abstract override fun getAccountDao(): AccountDao
    abstract override fun getTransactionDao(): TransactionDao
    abstract override fun getTransactionCategoryDao(): TransactionCategoryDao
    abstract override fun getCurrencyDao(): CurrencyDao
    abstract override fun getTagDao(): TagDao
    abstract override fun getTransactionTagDao(): TransactionTagDao
    abstract override fun getAccountTransactionDao(): AccountTransactionDao
    abstract override fun getScheduledTransactionDao(): ScheduledTransactionDao
    abstract override fun getScheduledTransactionTagDao(): ScheduledTransactionTagDao
    abstract override fun getCategoryBudgetDao(): CategoryBudgetDao
}

internal fun<T : RoomDatabase> RoomDatabase.Builder<T>.addConfig(
    assetReader: AssetReader,
    sqlQueryBuilder: SQLQueryBuilder
): RoomDatabase.Builder<T> {
    val currencyMetadataString = assetReader.read("currency_metadata.json")
    val currencyMetadataJson =
        Json.parseToJsonElement(currencyMetadataString) as? JsonObject
    val initCurrencyMetadataScript = "$INIT_CURRENCY_METADATA_SCRIPT ${sqlQueryBuilder.build(currencyMetadataJson as JsonObject)}"
    this.addMigrations(*migrationList().toTypedArray())
        .addCallback(object : Callback() {
            override fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)
                connection.execSQL(initCurrencyMetadataScript)
                connection.execSQL(INIT_ACCOUNT_GROUP_SCRIPT)
//                        db.execSQL(INIT_ACCOUNT_SCRIPT)
                connection.execSQL(INIT_TRANSACTION_TYPE_SCRIPT)
//                        db.execSQL(INIT_TRANSACTION_CATEGORY_SCRIPT)
            }
        })
    this.setDriver(BundledSQLiteDriver())
    return this
}
private fun migrationList(): List<Migration> {
    return listOf(
//        Migration_1_2,
        Migration_2_3,
//        Migration_3_4
        Migration_5_6
    )
}

const val INIT_ACCOUNT_GROUP_SCRIPT =
    "INSERT INTO AccountGroupEntity (id, name) VALUES (0, '${AccountGroupName.BANK_ACCOUNT}'), (1, '${AccountGroupName.CASH}'), (2, '${AccountGroupName.CREDIT_CARD}'), (3, '${AccountGroupName.DEBIT_CARD}'), (4, '${AccountGroupName.INSURANCE}'), (5, '${AccountGroupName.INVESTMENT}'), (6, '${AccountGroupName.LOAN}'), (7, '${AccountGroupName.PREPAID}'), (8, '${AccountGroupName.SAVING}'), (9, '${AccountGroupName.OTHERS}')"
const val INIT_ACCOUNT_SCRIPT =
    "INSERT INTO AccountEntity (name, account_group_id, currency) VALUES ('Cash', 1, 'MYR')"
const val INIT_TRANSACTION_TYPE_SCRIPT =
    "INSERT INTO TransactionTypeEntity (code) VALUES ('INCOME'), ('EXPENSES'), ('TRANSFER'), ('UPDATE_ACCOUNT')"
const val INIT_TRANSACTION_CATEGORY_SCRIPT =
    "INSERT INTO TransactionCategoryEntity (id, transaction_type_code, name, parent_id) VALUES (1, 'EXPENSES', 'Food', 0), (2, 'INCOME', 'Salary', 0), (3, 'EXPENSES', 'Rental', 0), (4, 'EXPENSES', 'Lunch', 1)"
const val INIT_CURRENCY_METADATA_SCRIPT =
    "INSERT INTO CurrencyMetaDataEntity (currencyCode, name, defaultFractionDigit) VALUES"