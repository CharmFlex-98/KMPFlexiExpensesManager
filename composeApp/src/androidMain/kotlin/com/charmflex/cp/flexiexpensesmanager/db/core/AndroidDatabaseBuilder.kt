package com.charmflex.cp.flexiexpensesmanager.db.core

import android.content.Context
import androidx.room.Room
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.db.addConfig
import com.charmflex.flexiexpensesmanager.features.currency.data.utils.SQLQueryBuilder

internal class AndroidDatabaseBuilder(
    private val appContext: Context,
    private val assetReader: AssetReader,
    private val sqlQueryBuilder: SQLQueryBuilder
) : DatabaseBuilder {
    override fun build(): DatabaseBuilder.IDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "FlexiExpensesManagerDB"
        )
            .addConfig(assetReader, sqlQueryBuilder)
            .build()
    }
}