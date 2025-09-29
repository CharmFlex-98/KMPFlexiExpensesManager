package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import kotlinx.serialization.Serializable

@Serializable
object AnnouncementRoute {
    @Serializable
    data class Root(
        val remoteConfigScene: RemoteConfigScene
    ) : NavigationRoute
}