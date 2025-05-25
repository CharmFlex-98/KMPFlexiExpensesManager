package com.charmflex.cp.flexiexpensesmanager.core.di

import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.flexiexpensesmanager.core.tracker.EventTracker
import org.jetbrains.compose.resources.Resource

internal interface MainInjector {
    val routeNavigator: RouteNavigator
    val eventTracker: EventTracker
}