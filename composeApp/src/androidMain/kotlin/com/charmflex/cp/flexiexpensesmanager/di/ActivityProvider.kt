package com.charmflex.cp.flexiexpensesmanager.di

import android.app.Activity
import java.lang.ref.WeakReference

internal class ActivityProvider {

    val currentActivity: Activity?
        get() {
        activityStack.removeIf { it.get() == null }  // Clean dead refs
        return activityStack.lastOrNull()?.get()
    }

    private val activityStack = arrayListOf<WeakReference<Activity>>()

    fun registerActivity(activity: Activity) {
        unregisterActivity(activity)
        activityStack.add(WeakReference(activity))
    }

    fun unregisterActivity(activity: Activity) {
        activityStack.removeIf { it.get() == activity }
    }
}