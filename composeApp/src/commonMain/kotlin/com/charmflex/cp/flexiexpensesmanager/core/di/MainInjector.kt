package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.flexiexpensesmanager.core.tracker.EventTracker

internal interface MainInjector {
    val routeNavigator RouteNavigator
    val eventTracker: EventTracker
}