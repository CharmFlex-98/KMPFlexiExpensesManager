package com.charmflex.cp.flexiexpensesmanager.features.auth.data

import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClient
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.put
import com.charmflex.cp.flexiexpensesmanager.features.auth.data.model.UpdateUserInfoRequest
import com.charmflex.cp.flexiexpensesmanager.features.auth.data.model.toDeviceInfo
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model.UserInfo
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository.AuthRepository
import com.charmflex.cp.flexiexpensesmanager.features.auth.data.model.UpdateUserInfoResponse
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.device.DeviceInfoService
import kotlin.time.ExperimentalTime

internal class AuthRepositoryImpl(
    private val deviceInfoService: DeviceInfoService,
    private val networkClient: NetworkClient
) : AuthRepository {
    @OptIn(ExperimentalTime::class)
    override suspend fun upsertUser(
        uid: String,
        username: String?,
        email: String?
    ): UserInfo {
        val res: UpdateUserInfoResponse = networkClient.put(
            "/auth/users/login",
            UpdateUserInfoRequest(
                uid,
                username,
                email,
                deviceInfoService.getDevice().toDeviceInfo()
            )
        )
        return UserInfo(
            uid = uid,
            username = username,
            email = email,
            createdAt = res.createdAt,
            isNewUser = res.newUser
        )
    }
}