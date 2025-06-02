package com.charmflex.cp.flexiexpensesmanager.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.NavigationRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.HomeRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.navigateAndPopUpTo
import com.charmflex.cp.flexiexpensesmanager.core.utils.navigateTo
import com.charmflex.cp.flexiexpensesmanager.core.utils.popWithArgs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal interface RouteNavigator {

    val navigationEvent: Flow<NavigationEvent>
    fun navigateTo(navigationRoute: NavigationRoute)
    fun navigateAndPopUpTo(route: NavigationRoute, popUpToRouteInclusive: NavigationRoute)
    fun pop()
    fun popWithArguments(data: Map<String, Any>)

    companion object {
        val instance by lazy { RouteNavigatorImpl() }
    }
}

internal class RouteNavigatorImpl : RouteNavigator {
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    override val navigationEvent: Flow<NavigationEvent>
        get() = _navigationEvent.asSharedFlow()

    override fun navigateTo(navigationRoute: NavigationRoute) {
        _navigationEvent.tryEmit(NavigateTo(navigationRoute))
    }

    override fun navigateAndPopUpTo(route: NavigationRoute, popUpToRouteInclusive: NavigationRoute) {
        _navigationEvent.tryEmit(NavigateAndPopUpTo(route = route, popToRouteInclusive = popUpToRouteInclusive))
    }

    override fun pop() {
        _navigationEvent.tryEmit(Pop)
    }

    override fun popWithArguments(data: Map<String, Any>) {
        _navigationEvent.tryEmit(PopWithArguments(data))
    }
}

sealed interface NavigationEvent

internal data class NavigateTo(
    val navigationRoute: NavigationRoute
) : NavigationEvent
internal data class NavigateAndPopUpTo(val route: NavigationRoute, val popToRouteInclusive: NavigationRoute): NavigationEvent
object Pop : NavigationEvent
data class PopWithArguments(
    val data: Map<String, Any>
) : NavigationEvent

@Composable
internal fun RouteNavigatorListener(
    routeNavigator: RouteNavigator,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        routeNavigator.navigationEvent.collect {
            when (it) {
                is NavigateTo -> navController.navigateTo(it.navigationRoute)
                is NavigateAndPopUpTo -> navController.navigateAndPopUpTo(it.route, it.popToRouteInclusive)
                is Pop -> navController.popBackStack()
                is PopWithArguments -> navController.popWithArgs(it.data)
            }
        }
    }
}

internal fun RouteNavigator.popWithHomeRefresh() {
    this.popWithArguments(
        mapOf(
            HomeRoutes.Args.HOME_REFRESH to true
        )
    )
}