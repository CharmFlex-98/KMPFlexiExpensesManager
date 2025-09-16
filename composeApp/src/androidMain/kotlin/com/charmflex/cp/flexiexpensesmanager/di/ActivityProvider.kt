package com.charmflex.cp.flexiexpensesmanager.di

import android.app.Activity
import java.lang.ref.WeakReference

internal class ActivityProvider {
    private var _currentActivity: WeakReference<Activity>? = null
    val currentActivity = _currentActivity?.get()

    fun registerActivity(activity: Activity) {
        _currentActivity = WeakReference(activity)
    }

    fun unregisterActivity(activity: Activity) {
        if (_currentActivity?.get() == activity) {
            _currentActivity = null
        }
    }
}