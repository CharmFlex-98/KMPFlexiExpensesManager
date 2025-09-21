package com.charmflex.cp.flexiexpensesmanager.core.app

import com.charmflex.cp.flexiexpensesmanager.BuildConfig

internal class AndroidAppConfigProvider : AppConfigProvider {
    override fun getAppFlavour(): AppFlavour? {
        return when (BuildConfig.FLAVOR) {
            "paid" -> AppFlavour.PAID
            "free" -> AppFlavour.FREE
            else -> null
        }
    }
}