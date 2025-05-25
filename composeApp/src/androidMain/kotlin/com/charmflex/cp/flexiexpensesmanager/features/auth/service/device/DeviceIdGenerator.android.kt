package com.charmflex.cp.flexiexpensesmanager.features.auth.service.device

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.UUID

internal class AndroidDeviceIdGenerator(
    private val appContext: Context
) : DeviceIdGenerator {
    override fun generateDeviceId(): String {
        val rawDeviceId = listOf(
            Build.BOARD, Build.BRAND, Build.DEVICE, Build.HARDWARE,
            Build.ID, Build.MANUFACTURER, Build.MODEL, Build.PRODUCT,
            Settings.Secure.getString(appContext.contentResolver, Settings.Secure.ANDROID_ID)
        ).joinToString("-")

        return hashString(rawDeviceId)
    }

    private fun hashString(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(input.toByteArray(StandardCharsets.UTF_8))
            hash.joinToString("") { "%02x".format(it) } // Convert to hex
        } catch (e: Exception) {
            UUID.randomUUID().toString() // Fallback to random UUID
        }
    }
}