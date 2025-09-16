package com.charmflex.cp.flexiexpensesmanager

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.charmflex.cp.flexiexpensesmanager.di.ActivityProvider
import com.charmflex.cp.flexiexpensesmanager.di.AppComponent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.di.KoinInitializer
import org.koin.android.ext.android.getKoin
import org.koin.core.component.KoinComponent

internal class FlexiExpensesManagerApp : Application(), AppComponentProvider, ActivityLifecycleCallbacks {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(this).init()
        appComponent = AppComponent()
        AppComponentProvider.instance = this

        registerActivityLifecycleCallbacks(this)
    }

    override fun getAppComponent(): AppComponent {
        return appComponent ?: throw RuntimeException("KoinComponent is not yet initialized!")
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        val provider = getKoin().get<ActivityProvider>()
        provider.registerActivity(p0)
    }

    override fun onActivityStarted(p0: Activity) {

    }

    override fun onActivityResumed(p0: Activity) {

    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        val provider = getKoin().get<ActivityProvider>()
        provider.unregisterActivity(p0)
    }
}