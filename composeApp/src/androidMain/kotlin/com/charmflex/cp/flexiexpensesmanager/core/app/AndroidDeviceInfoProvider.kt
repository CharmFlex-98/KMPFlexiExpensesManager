package com.charmflex.cp.flexiexpensesmanager.core.app

import android.content.res.Resources
import java.util.Locale


internal class AndroidDeviceInfoProvider : DeviceInfoProvider {
    override fun locale(): String {
        val locale: Locale =
            Resources.getSystem().configuration.locales[0]
        return locale.toLanguageTag()
    }
}