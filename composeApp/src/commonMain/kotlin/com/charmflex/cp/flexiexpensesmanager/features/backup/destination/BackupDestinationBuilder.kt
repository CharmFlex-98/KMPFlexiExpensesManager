package com.charmflex.cp.flexiexpensesmanager.features.backup.destination

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BackupRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.features.backup.ui.ImportDataScreen

internal class BackupDestinationBuilder : DestinationBuilder {

    private val appComponent = AppComponentProvider.instance.getAppComponent()
    override fun NavGraphBuilder.buildGraph() {
        importSetting()
    }
    
    private fun NavGraphBuilder.importSetting() {
        composable<BackupRoutes.ImportSetting> {
            val updateImportedData = it.savedStateHandle.remove<Boolean>(BackupRoutes.Args.UPDATE_IMPORT_DATA) ?: false
            val viewModel = getViewModel {
                appComponent.importDataViewModel
            }
            LaunchedEffect(key1 = updateImportedData) {
                if (updateImportedData) viewModel.updateImportedData()
            }
            ImportDataScreen(importDataViewModel = viewModel)
        }
    }
}