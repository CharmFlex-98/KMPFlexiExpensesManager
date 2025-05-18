package com.charmflex.cp.flexiexpensesmanager.di

internal interface AppComponentProvider {
    companion object {
        lateinit var instance: AppComponentProvider
    }
    fun getAppComponent(): AppComponent
}