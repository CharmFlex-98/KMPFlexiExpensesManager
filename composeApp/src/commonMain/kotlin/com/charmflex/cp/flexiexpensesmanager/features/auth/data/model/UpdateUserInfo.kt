package com.charmflex.cp.flexiexpensesmanager.features.auth.data.model

import com.charmflex.cp.flexiexpensesmanager.features.auth.data.model.UpdateUserInfoRequest.DeviceInfo
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model.Device
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal data class UpdateUserInfoRequest(
    val uid: String,
    val username: String?,
    val email: String?,
    val deviceInfo: DeviceInfo?
) {
    data class DeviceInfo(
        val deviceId: String,
        val brand: String,
        val model: String,
        val sdkVersion: Int,
        val androidVersion: String
    )
}

internal data class UpdateUserInfoResponse @OptIn(ExperimentalTime::class) constructor(
    val createdAt: Instant,
    val newUser: Boolean
)

internal fun Device.toDeviceInfo(): DeviceInfo {
    return DeviceInfo(
        this.deviceId,
        this.brand,
        this.model,
        this.sdkVersion,
        this.platformVersion
    )
}