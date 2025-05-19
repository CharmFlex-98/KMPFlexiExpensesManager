package com.charmflex.cp.flexiexpensesmanager.features.auth.data

import com.charmflex.cp.flexiexpensesmanager.core.di.networkModule
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClientBuilder
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.put
import com.charmflex.flexiexpensesmanager.features.auth.data.model.UpdateUserInfoRequest
import com.charmflex.flexiexpensesmanager.features.auth.data.model.toDeviceInfo
import com.charmflex.flexiexpensesmanager.features.auth.data.remote.AuthApi
import com.charmflex.flexiexpensesmanager.features.auth.domain.model.UserInfo
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository.AuthRepository
import com.charmflex.flexiexpensesmanager.features.auth.data.model.UpdateUserInfoResponse
import com.charmflex.flexiexpensesmanager.features.auth.service.device.DeviceInfoService

internal class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val deviceInfoService: DeviceInfoService,
    private val networkClient: NetworkClientBuilder.NetworkClient
) : AuthRepository {
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