package com.charmflex.cp.flexiexpensesmanager.di

import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.di.androidMainModule
import com.charmflex.cp.flexiexpensesmanager.db.di.androidDbModule
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.module.authModuleAndroid
import com.charmflex.cp.flexiexpensesmanager.features.backup.di.modules.androidBackupModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

actual class KoinInitializer(
    private val appContext: Context
) {
    actual fun init() {
        startKoin {
            androidContext(appContext)
            // Android-specific
            modules(androidDbModule, authModuleAndroid, androidBackupModules, androidMainModule)

            // Common
            modules(commonModules())
        }
    }
}