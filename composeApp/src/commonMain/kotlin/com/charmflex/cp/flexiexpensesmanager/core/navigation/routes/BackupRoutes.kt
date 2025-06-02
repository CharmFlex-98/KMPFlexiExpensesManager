package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.NavigationRoute
import kotlinx.serialization.Serializable

object BackupRoutes {
    object Args {
        const val UPDATE_IMPORT_DATA = "update_import_data"
    }

    @Serializable
    object ImportSetting : NavigationRoute

//    fun importSettingDestination(): String {
//        return buildDestination(IMPORT_SETTING_ROUTE) {
//            withArg()
//        }
//    }
}