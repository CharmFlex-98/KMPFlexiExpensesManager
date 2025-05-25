package com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model

internal data class Device(
    val deviceId: String,
    val brand: String,
    val model: String,
    val sdkVersion: Int,
    val platformVersion: String
)