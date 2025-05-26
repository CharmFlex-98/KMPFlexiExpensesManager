package com.charmflex.cp.flexiexpensesmanager.features.auth.service.device

import android.os.Build
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model.Device
import com.charmflex.cp.flexiexpensesmanager.features.auth.storage.AuthStorage

internal actual class DeviceInfoService  constructor(
    private val deviceIdGenerator: DeviceIdGenerator,
    private val authStorage: AuthStorage
) {
    actual fun getDevice(): Device {
        return Device(
            deviceId = getDeviceId(),
            brand = Build.BRAND,
            model = Build.MODEL,
            sdkVersion = Build.VERSION.SDK_INT,
            platformVersion = Build.VERSION.RELEASE
        )
    }

    private fun getDeviceId(): String {
        val storedId = authStorage.getDeviceID()

        if (storedId.isNotBlank()) {
            return storedId  // Return stored ID if already exists
        }

        // Generate a new device ID
        val newDeviceId = generateDeviceId()

        // Store it persistently in SharedPreferences
        authStorage.saveDeviceID(newDeviceId)

        return newDeviceId
    }

    private fun generateDeviceId(): String {
        return deviceIdGenerator.generateDeviceId()
    }
}