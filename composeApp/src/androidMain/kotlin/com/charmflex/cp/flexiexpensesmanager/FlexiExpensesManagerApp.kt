package com.charmflex.cp.flexiexpensesmanager

import android.app.Application
import com.charmflex.cp.flexiexpensesmanager.di.AppComponent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.di.KoinInitializer
import org.koin.core.component.KoinComponent

internal class FlexiExpensesManagerApp : Application(), AppComponentProvider {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(this).init()
        appComponent = AppComponent()
        AppComponentProvider.instance = this

        initIAP()
    }

    override fun getAppComponent(): AppComponent {
        return appComponent ?: throw RuntimeException("KoinComponent is not yet initialized!")
    }

    private fun initIAP() {

    }
}