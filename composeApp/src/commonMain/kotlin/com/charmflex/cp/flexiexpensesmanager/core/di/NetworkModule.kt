package com.charmflex.flexiexpensesmanager.core.di

import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.network.DefaultNetworkClientBuilder
import com.charmflex.cp.flexiexpensesmanager.core.network.NetworkClientBuilder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface NetworkModule {

    companion object {
        @Provides
        @Singleton
        fun providesNetworkClientBuilder(appContext: Context): NetworkClientBuilder {
            return DefaultNetworkClientBuilder(
                appContext
            )
        }
    }
}