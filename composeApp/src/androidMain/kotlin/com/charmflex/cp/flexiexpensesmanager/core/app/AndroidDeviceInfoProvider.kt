package com.charmflex.cp.flexiexpensesmanager.core.app

import android.content.res.Resources


internal class AndroidDeviceInfoProvider : DeviceInfoProvider {
    override fun locale(): String {
        val locale = Resources.getSystem().configuration.locales[0]
        val language = locale.language // "zh"
        val country = locale.country // "CN"
        val localeString = if (country.isNotEmpty()) {
            language + "_" + country // "zh_CN"
        } else {
            language // "zh"
        }

        return localeString
    }
}