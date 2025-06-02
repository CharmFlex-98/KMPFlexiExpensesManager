package com.charmflex.cp.flexiexpensesmanager.core.utils

import androidx.navigation.NavController
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.NavigationRoute

internal fun NavController.navigateTo(navigationRoute: NavigationRoute) {
//    args?.let {
//        this.currentBackStackEntry?.savedStateHandle?.let { savedStateHandler ->
//            for (arg in args) {
//                savedStateHandler[arg.key] = arg.value
//            }
//        }
//    }
    navigate(navigationRoute) {
        launchSingleTop = true
    }
}

internal fun NavController.navigateAndPopUpTo(route: NavigationRoute, popUpToRouteInclusive: NavigationRoute? = null) {
    navigate(route) {
        launchSingleTop = true

        if (popUpToRouteInclusive != null) {
            popUpTo(popUpToRouteInclusive) {
                inclusive = true
            }
        }

    }
}

fun NavController.popWithArgs(data: Map<String, Any>) {
    data.let { args ->
        this.previousBackStackEntry?.savedStateHandle?.let { savedStateHandler ->
            for (arg in args) {
                savedStateHandler[arg.key] = arg.value
            }
        }
    }

    popBackStack()
}