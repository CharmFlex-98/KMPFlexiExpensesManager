package com.charmflex.cp.flexiexpensesmanager.db.core

import androidx.room.Room
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import com.charmflex.cp.flexiexpensesmanager.db.AppDatabase
import com.charmflex.cp.flexiexpensesmanager.db.addConfig
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.utils.SQLQueryBuilder

internal class IOSDatabaseBuilder(
    private val assetReader: AssetReader,
    private val sqlQueryBuilder: SQLQueryBuilder
) : DatabaseBuilder {
    override fun build(): DatabaseBuilder.IDatabase {
        return Room.databaseBuilder<AppDatabase>(
            "FlexiExpensesManagerDB"
        )
            .addConfig(assetReader, sqlQueryBuilder)
            .build()
    }
}