package com.charmflex.cp.flexiexpensesmanager.features.tag.destination

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getString
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TagRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.tag.ui.TagSettingScreen

internal class TagDestinationBuilder : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }

    override fun NavGraphBuilder.buildGraph() {
        tagSetting()
        importTagSetting()
    }

    private fun NavGraphBuilder.importTagSetting() {
        composable<TagRoutes.TagEditorDefault>(
        ) {
            val route = it.toRoute<TagRoutes.TagEditorDefault>()
            getTagScreen(route)
        }
    }

    private fun NavGraphBuilder.tagSetting() {
        composable<TagRoutes.ImportTagEditor>(
        ) {
            val route = it.toRoute<TagRoutes.ImportTagEditor>()
            getTagScreen(route)
        }
    }

    @Composable
    private fun getTagScreen(route: TagRoutes.TagEditorRoute) {
        val viewModel = when (route) {
            is TagRoutes.ImportTagEditor -> {
                getViewModel {
                    appComponent.tagSettingViewModel().apply {
                        initFlow(route.tagName)
                    }
                }
            }
            else -> {
               getViewModel {
                    appComponent.tagSettingViewModel().apply {
                        initFlow(null)
                    }
                }
            }
        }
        TagSettingScreen(viewModel = viewModel)
    }
}