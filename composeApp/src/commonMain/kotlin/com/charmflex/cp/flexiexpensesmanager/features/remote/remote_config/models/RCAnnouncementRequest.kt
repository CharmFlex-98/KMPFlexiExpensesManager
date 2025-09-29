package com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models

import kotlinx.serialization.Serializable

@Serializable
enum class RemoteConfigScene {
    HOME, TAG, REFERRAL
}

@Serializable
data class RCAnnouncementRequest(
    val scene: RemoteConfigScene
)