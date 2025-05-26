package com.charmflex.cp.flexiexpensesmanager.db.di

import com.charmflex.cp.flexiexpensesmanager.db.core.AndroidDatabaseBuilder
import com.charmflex.cp.flexiexpensesmanager.db.core.DatabaseBuilder
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.utils.SQLQueryBuilder
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val androidDbModule = module {
    factory { SQLQueryBuilder() }
    singleOf(::AndroidDatabaseBuilder) { bind<DatabaseBuilder>() }
    single<DatabaseBuilder.IDatabase> { get<DatabaseBuilder>().build() }
}