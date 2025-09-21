package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

object SchedulerRoutes {
    @Serializable
    object SchedulerList : NavigationRoute

    @Serializable
    data class SchedulerDetail(
        val id: Long
    ) : NavigationRoute
}