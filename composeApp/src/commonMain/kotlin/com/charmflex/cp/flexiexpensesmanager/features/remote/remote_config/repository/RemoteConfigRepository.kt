package com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.repository

import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.features.announcement.ui.ANNOUNCEMENT_DO_NOT_SHOW_AGAIN_PREFIX
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.RemoteConfigApi
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import org.koin.core.annotation.Singleton

@Singleton
internal class RemoteConfigRepository(
    private val remoteConfigApi: RemoteConfigApi,
) {
    suspend fun getSceneAnnouncement(remoteConfigRequest: RCAnnouncementRequest): RCAnnouncementResponse? {
        return remoteConfigApi.getRemoteConfigAnnouncement(remoteConfigRequest)
    }
}