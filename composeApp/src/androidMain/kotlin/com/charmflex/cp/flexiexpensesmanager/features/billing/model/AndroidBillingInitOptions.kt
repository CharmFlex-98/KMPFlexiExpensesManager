package com.charmflex.cp.flexiexpensesmanager.features.billing.model

import android.app.Activity

internal class AndroidBillingInitOptions(
    private val activity: Activity
) : InitOptions {
    fun getContext(): Activity {
        return activity
    }
}