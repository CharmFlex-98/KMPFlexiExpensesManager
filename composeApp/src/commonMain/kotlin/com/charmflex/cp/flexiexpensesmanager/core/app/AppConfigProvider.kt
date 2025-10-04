package com.charmflex.cp.flexiexpensesmanager.core.app

import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppBuildType
import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppFlavor

internal interface AppConfigProvider {
    fun getAppFlavour(): AppFlavor?
    fun getAppBuildType(): AppBuildType
    fun getAppVersion(): String
    fun baseUrl(): String
    fun signaturePemFilePath(): String
    fun packageName(): String
}