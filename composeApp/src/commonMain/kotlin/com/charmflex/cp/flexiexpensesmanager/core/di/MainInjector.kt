package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker

internal interface MainInjector {
    fun routeNavigator(): RouteNavigator
}