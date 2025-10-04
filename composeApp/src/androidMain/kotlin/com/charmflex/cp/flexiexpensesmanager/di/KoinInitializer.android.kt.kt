package com.charmflex.cp.flexiexpensesmanager.di

import android.app.Activity
import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.di.androidMainModule
import com.charmflex.cp.flexiexpensesmanager.db.di.androidDbModule
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.module.authModuleAndroid
import com.charmflex.cp.flexiexpensesmanager.features.backup.di.modules.androidBackupModules
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.bind
import org.koin.dsl.koinConfiguration
import org.koin.dsl.module

actual class KoinInitializer(
    private val appContext: Context,
) {
    actual fun init() {
        startKoin {
            internalInit()
        }
    }

    actual fun initAsync(): KoinConfiguration {
        return koinConfiguration {
            internalInit()
        }
    }

    private fun KoinApplication.internalInit() {
        androidContext(appContext)
        // Android-specific
        modules(androidDbModule, authModuleAndroid, androidBackupModules, androidMainModule)

        // Common
        modules(commonModules())
    }
}