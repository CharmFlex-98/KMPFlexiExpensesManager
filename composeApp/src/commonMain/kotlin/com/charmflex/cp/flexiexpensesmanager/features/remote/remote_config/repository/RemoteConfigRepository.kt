package com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.repository

import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.RemoteConfigApi
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import org.koin.core.annotation.Singleton

@Singleton
internal class RemoteConfigRepository(
    private val remoteConfigApi: RemoteConfigApi
) {
    suspend fun getSceneAnnouncement(remoteConfigRequest: RCAnnouncementRequest): RCAnnouncementResponse {
        return remoteConfigApi.getRemoteConfigAnnouncement(remoteConfigRequest)
    }
}