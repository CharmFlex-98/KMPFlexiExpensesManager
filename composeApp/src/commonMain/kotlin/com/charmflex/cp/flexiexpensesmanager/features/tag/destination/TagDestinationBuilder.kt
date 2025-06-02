package com.charmflex.cp.flexiexpensesmanager.features.tag.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
            val importFixTagName = it.arguments?.getString(TagRoutes.Args.IMPORT_FIX_TAG_NAME)
            val viewModel = getViewModel {
                appComponent.tagSettingViewModel.apply {
                    initFlow(importFixTagName)
                }
            }
            TagSettingScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.tagSetting() {
        composable<TagRoutes.ImportTagEditor>(
        ) {
            val importFixTagName = it.arguments?.getString(TagRoutes.Args.IMPORT_FIX_TAG_NAME)
            val viewModel = getViewModel {
                appComponent.tagSettingViewModel.apply {
                    initFlow(importFixTagName)
                }
            }
            TagSettingScreen(viewModel = viewModel)
        }
    }
}