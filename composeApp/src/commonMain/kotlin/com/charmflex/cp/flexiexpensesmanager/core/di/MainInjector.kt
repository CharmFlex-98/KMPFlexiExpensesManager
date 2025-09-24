package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager

internal interface MainInjector {
    fun routeNavigator(): RouteNavigator
    fun toastManager(): ToastManager
}