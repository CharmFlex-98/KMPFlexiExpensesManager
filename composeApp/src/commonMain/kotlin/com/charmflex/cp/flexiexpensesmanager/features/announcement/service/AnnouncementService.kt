package com.charmflex.cp.flexiexpensesmanager.features.announcement.service

import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.features.announcement.ui.ANNOUNCEMENT_DO_NOT_SHOW_AGAIN_PREFIX
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.repository.RemoteConfigRepository
import org.koin.core.annotation.Singleton

@Singleton
internal class AnnouncementService(
    private val announcementRepository: RemoteConfigRepository,
    private val sharedPrefs: SharedPrefs
) {
    suspend fun getSceneAnnouncement(remoteConfigRequest: RCAnnouncementRequest): RCAnnouncementResponse? {
        val res = announcementRepository.getSceneAnnouncement(remoteConfigRequest)
        res?.let {
            if (sharedPrefs.getBoolean(ANNOUNCEMENT_DO_NOT_SHOW_AGAIN_PREFIX + remoteConfigRequest.scene + "_" + it.version, false)) {
                return null
            }
        }

        return res
    }

    fun doNotShowAgainScene(scene: RemoteConfigScene, version: Int) {
        sharedPrefs.setBoolean(ANNOUNCEMENT_DO_NOT_SHOW_AGAIN_PREFIX + scene + "_" + version, true)
    }
}