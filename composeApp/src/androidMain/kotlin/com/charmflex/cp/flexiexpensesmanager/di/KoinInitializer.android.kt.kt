package com.charmflex.cp.flexiexpensesmanager.di

import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.di.androidMainModule
import com.charmflex.cp.flexiexpensesmanager.db.di.androidDbModule
import com.charmflex.cp.flexiexpensesmanager.feature.auth.di.module.authModuleAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

actual class KoinInitializer(
    private val appContext: Context
) {
    actual fun init() {
        startKoin {
            androidContext(appContext)
            // Android-specific
            modules(androidDbModule, authModuleAndroid, androidMainModule)

            // Common
            modules(commonModules())
        }
    }
}