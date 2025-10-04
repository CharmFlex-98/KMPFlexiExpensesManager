package com.charmflex.cp.flexiexpensesmanager.core.app

import com.charmflex.cp.flexiexpensesmanager.BuildConfig
import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppBuildType
import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppFlavor

internal class AndroidAppConfigProvider : AppConfigProvider {
    override fun getAppFlavour(): AppFlavor {
        return when (BuildConfig.FLAVOR) {
            "paid" -> AppFlavor.PAID
            "free" -> AppFlavor.FREE
            else -> AppFlavor.FREE
        }
    }

    override fun getAppBuildType(): AppBuildType {
        return when (BuildConfig.BUILD_TYPE) {
            "debug" -> AppBuildType.DEBUG
            "release" -> AppBuildType.RELEASE
            else -> AppBuildType.DEBUG
        }
    }

    override fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun baseUrl(): String {
        return BuildConfig.SERVER_URL
    }

    override fun signaturePemFilePath(): String {
        return BuildConfig.PUB_SIGN_PEM_PATH
    }
}