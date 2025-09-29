package com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models

import kotlinx.serialization.Serializable


enum class IconType {
    ANNOUNCEMENT, COMING_SOON, IN_PROGRESS
}

enum class ActionType {
    CLOSE, UPDATE_AT_STORE, BACK
}

@Serializable
data class RCAnnouncementResponse(
    val scene: RemoteConfigScene,
    val title: String,
    val subtitle: String,
    val label: String,
    val closable: Boolean,
    val iconType: IconType,
    val actionType: ActionType,
    val show: Boolean
)