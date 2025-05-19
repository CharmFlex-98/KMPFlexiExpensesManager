package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClientBuilder
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.KtorNetworkClientBuilder
import org.koin.dsl.bind
import org.koin.dsl.module


val networkModule = module {
    single {
        val networkClient = KtorNetworkClientBuilder()
            .baseUrl("http://fem.charmflex.com")
            .build()
        networkClient
    }.bind<NetworkClientBuilder.NetworkClient>()
}