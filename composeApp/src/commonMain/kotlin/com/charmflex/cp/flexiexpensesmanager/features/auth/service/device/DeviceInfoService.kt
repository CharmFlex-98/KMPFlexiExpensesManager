package com.charmflex.cp.flexiexpensesmanager.features.auth.service.device

import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model.Device

internal expect class DeviceInfoService {
    fun getDevice(): Device
}

