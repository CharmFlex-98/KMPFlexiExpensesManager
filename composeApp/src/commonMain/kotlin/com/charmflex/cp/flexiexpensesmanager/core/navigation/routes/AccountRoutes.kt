package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import kotlinx.serialization.Serializable


object AccountRoutes {

    object Args {
        const val IMPORT_FIX_ACCOUNT_NAME = "import_fix_account_name"
        const val ACCOUNT_ID = "account_id"
        const val ACCOUNT_DETAIL_DATE_FILTER = "account_detail_date_filter"
    }

    @Serializable
    internal object EditorAccountRouteDefault: NavigationRoute

    @Serializable
    internal data class ImportEditorAccountRoute(
        val fixAccountName: String
    ) : NavigationRoute

    @Serializable
    internal data class DetailAccountRoute(
        private val accountID: Int,
        private val dateFilter: DateFilter?
    ) : NavigationRoute

    internal fun editorDestination(importFixAccountName: String? = null): NavigationRoute {
        return importFixAccountName?.let {
            ImportEditorAccountRoute(it)
        } ?: EditorAccountRouteDefault
    }
    internal fun accountDetailDestination(accountId: Int, dateFilter: DateFilter? = null): NavigationRoute {
        return DetailAccountRoute(accountId, dateFilter)
    }
}