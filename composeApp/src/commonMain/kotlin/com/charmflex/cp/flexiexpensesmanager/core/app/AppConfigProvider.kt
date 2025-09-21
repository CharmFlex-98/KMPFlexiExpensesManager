package com.charmflex.cp.flexiexpensesmanager.core.app

internal interface AppConfigProvider {
    fun getAppFlavour(): AppFlavour?
}