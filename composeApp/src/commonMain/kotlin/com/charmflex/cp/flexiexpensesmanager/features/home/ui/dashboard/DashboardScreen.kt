package com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2

@Composable
internal fun DashboardScreen(
    viewModel: DashboardViewModel,
) {
    val plugins by viewModel.plugins.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(grid_x2)
    ) {
        plugins.forEach {
            with(it) { Render() }
        }
    }
}