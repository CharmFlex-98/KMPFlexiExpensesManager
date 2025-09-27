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
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.daos.AccountTransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.account.data.entities.AccountEntity
import com.charmflex.cp.flexiexpensesmanager.features.account.data.entities.AccountGroupEntity
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.daos.CategoryBudgetDao
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.entities.CategoryBudgetEntity
import com.charmflex.cp.flexiexpensesmanager.features.budget.data.entities.MonthlyCategoryBudgetEntity
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.daos.CurrencyDao
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.models.UserCurrencyRateEntity
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.daos.ScheduledTransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.entities.ScheduledTransactionEntity
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.data.entities.ScheduledTransactionTagEntity
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.daos.TagDao
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionDao
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.entities.TagEntity
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.daos.TransactionTagDao
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.entities.TransactionCategoryEntity
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.entities.CurrencyMetaDataEntity
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.utils.SQLQueryBuilder
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
//        Migration_2_3,
//        Migration_3_4
//        Migration_5_6
    )
}

val INIT_ACCOUNT_GROUP_SCRIPT =
    "INSERT INTO AccountGroupEntity (id, name) VALUES (0, '${AccountGroupEnum.BANK_ACCOUNT.value}'), (1, '${AccountGroupEnum.CASH.value}'), (2, '${AccountGroupEnum.CREDIT_CARD.value}'), (3, '${AccountGroupEnum.DEBIT_CARD.value}'), (4, '${AccountGroupEnum.INSURANCE.value}'), (5, '${AccountGroupEnum.INVESTMENT.value}'), (6, '${AccountGroupEnum.LOAN.value}'), (7, '${AccountGroupEnum.PREPAID.value}'), (8, '${AccountGroupEnum.SAVING.value}'), (9, '${AccountGroupEnum.OTHERS.value}')"
const val INIT_TRANSACTION_TYPE_SCRIPT =
    "INSERT INTO TransactionTypeEntity (code) VALUES ('INCOME'), ('EXPENSES'), ('TRANSFER'), ('UPDATE_ACCOUNT')"
const val INIT_CURRENCY_METADATA_SCRIPT =
    "INSERT INTO CurrencyMetaDataEntity (currencyCode, name, defaultFractionDigit) VALUES"