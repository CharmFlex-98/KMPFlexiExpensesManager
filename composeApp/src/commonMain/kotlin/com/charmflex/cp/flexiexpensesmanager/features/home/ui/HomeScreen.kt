package com.charmflex.cp.flexiexpensesmanager.features.home.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.HomeRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponent
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.account.AccountHomeScreen
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.account.AccountHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.history.TransactionHistoryHomeScreen
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard.DashboardScreen
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard.DashboardViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.history.TransactionHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_heat_map.ExpensesHeatMapPlugin
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.ExpensesChartDashboardPlugin
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting.SettingScreen
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting.SettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.ActionType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.IconType
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGBottomNavItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGBottomNavigationBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.theme.FlexiExpensesManagerTheme
import com.charmflex.cp.flexiexpensesmanager.ui_common.AnnouncementAnimation
import com.charmflex.cp.flexiexpensesmanager.ui_common.AnnouncementPanel
import com.charmflex.cp.flexiexpensesmanager.ui_common.BlurredBackgroundBox
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody1
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading1
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading3
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSmallPrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x16
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x20
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeScreen(
    appComponent: AppComponent,
    refreshOnHomeShown: Boolean
) {
    val expensesPieChartViewModel = getViewModel {
        appComponent.expensesPieChartViewModel()
    }
    val expensesHeatMapViewModel = getViewModel {
        appComponent.expensesHeatMapViewModel()
    }
    val dashboardPlugins = listOf(
        ExpensesChartDashboardPlugin(expensesPieChartViewModel),
        ExpensesHeatMapPlugin(expensesHeatMapViewModel)
    )

    // Dashboard
    val dashboardViewModel = getViewModel {
        appComponent.dashBoardViewModel().apply {
            initPlugins(
                dashboardPlugins
            )
        }
    }

    // Transaction History
    val transactionHomeViewModel = getViewModel {
        appComponent.expensesHistoryViewModel()
    }

    // Account Home
    val accountHomeViewModel = getViewModel {
        appComponent.accountHomeViewModel()
    }

    // Setting
    val settingViewModel = getViewModel {
        appComponent.settingViewModel()
    }

    val homeViewModel: HomeViewModel = getViewModel {
        appComponent.homeViewModel().apply {
            initHomeRefreshable(dashboardViewModel, transactionHomeViewModel, accountHomeViewModel)
        }
    }

    if (refreshOnHomeShown) {
        homeViewModel.notifyRefresh()
    }

    HomeBody(
        homeViewModel,
        dashboardViewModel,
        transactionHomeViewModel,
        accountHomeViewModel,
        settingViewModel
    )
}

@Composable
private fun HomeBody(
    homeViewModel: HomeViewModel,
    dashboardViewModel: DashboardViewModel,
    transactionHomeViewModel: TransactionHomeViewModel,
    accountHomeViewModel: AccountHomeViewModel,
    settingViewModel: SettingViewModel
) {
    val homeViewState by homeViewModel.viewState.collectAsState()
    val announcement = homeViewState.homeRCAnnouncementRequest
    val bottomNavController = rememberNavController()
    val showAnnouncement = homeViewState.homeRCAnnouncementRequest?.show == true

    BlurredBackgroundBox(
        blur = showAnnouncement
    ) {
        SGScaffold(
            modifier = Modifier.fillMaxSize().padding(grid_x2),
            bottomBar = { HomeScreenBottomNavigationBar(bottomBarNavController = bottomNavController) },
            floatingActionButton = {
                FloatingActionButton(onClick = homeViewModel::createNewExpenses) {
                    SGIcons.Add()
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            screenName = "HomeScreen"
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = HomeRoutes.SummaryHomeRoute
            ) {
                composable<HomeRoutes.SummaryHomeRoute> {
                    DashboardScreen(dashboardViewModel)
                }
                composable<HomeRoutes.DetailHomeRoute> {
                    TransactionHistoryHomeScreen(transactionHomeViewModel = transactionHomeViewModel)
                }
                composable<HomeRoutes.AccountsHomeRoute> {
                    AccountHomeScreen(viewModel = accountHomeViewModel)
                }
                composable<HomeRoutes.SettingHomeRoute> {
                    SettingScreen(viewModel = settingViewModel) {
                        homeViewModel.notifyRefresh()
                    }
                }
            }
        }
    }

    AnnouncementPanel(
        show = showAnnouncement,
        chipText = announcement?.label ?: "",
        iconType = announcement?.iconType ?: IconType.ANNOUNCEMENT,
        title = announcement?.title ?: "",
        subtitle = announcement?.subtitle ?: "",
        closable = announcement?.closable ?: true,
        onClosed = {
            homeViewModel.closeAnnouncement()
        }
    ) {
        homeViewModel.onAnnouncementAction()
    }
}


@Composable
fun HomeScreenPreview() {
    FlexiExpensesManagerTheme {
//        HomeScreen()
    }
}

@Composable
fun HomeScreenBottomNavigationBar(bottomBarNavController: NavController) {
    val currentBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val item = remember { bottomBarItem() }
    SGBottomNavigationBar(
        tonalElevation = 0.dp,
        items = item,
        isSelected = { selected ->
            currentBackStackEntry?.destination?.hierarchy?.any { it.hasRoute(selected::class) } == true
        }
    ) {
        bottomBarNavController.navigate(it.route) {
            popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                saveState = true
            }
            // Restore state when re-selecting a previously backstack pop by popUpTo
            restoreState = true

            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
        }
    }
}

internal fun bottomBarItem(): List<SGBottomNavItem> {
    return listOf(
        SGBottomNavItem(
            index = 0,
            titleId = Res.string.home_bar_item_summary,
            iconId = Res.drawable.ic_pie_chart,
            route = HomeRoutes.SummaryHomeRoute,
        ),
        SGBottomNavItem(
            index = 1,
            titleId = Res.string.home_bar_item_details,
            iconId = Res.drawable.ic_wallet,
            route = HomeRoutes.DetailHomeRoute
        ),
        SGBottomNavItem(
            index = 2,
            titleId = Res.string.home_bar_item_accounts,
            iconId = Res.drawable.ic_wallet,
            route = HomeRoutes.AccountsHomeRoute
        ),
        SGBottomNavItem(
            index = 3,
            titleId = Res.string.home_bar_item_setting,
            iconId = Res.drawable.icon_people,
            route = HomeRoutes.SettingHomeRoute
        )
    )
}