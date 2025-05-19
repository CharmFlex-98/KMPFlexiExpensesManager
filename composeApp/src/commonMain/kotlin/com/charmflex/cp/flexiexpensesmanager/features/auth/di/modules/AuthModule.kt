package com.charmflex.cp.flexiexpensesmanager.features.auth.di.modules
import com.charmflex.cp.flexiexpensesmanager.features.auth.data.AuthRepositoryImpl
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository.AuthRepository
import com.charmflex.flexiexpensesmanager.features.auth.storage.AuthStorage
import com.charmflex.flexiexpensesmanager.features.auth.storage.AuthStorageImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val authModule = module {
    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
    singleOf(::AuthStorageImpl) { bind<AuthStorage>() }
}