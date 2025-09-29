package com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config

import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkAttributes
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClient
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.get
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.post
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
internal class RemoteConfigApi(
    private val networkClient: NetworkClient
) {
    suspend fun getRemoteConfigAnnouncement(remoteConfigRequest: RCAnnouncementRequest): RCAnnouncementResponse {
        return withContext(Dispatchers.IO) {
            networkClient.post("/api/v1/remote-config/announcement", remoteConfigRequest) {
                add(NetworkAttributes.verifySignature)
            }
        }
    }
}