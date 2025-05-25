package com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.ExpensesChartScreen
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.ExpensesChartViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard.DashboardPlugin

internal class ExpensesChartDashboardPlugin  constructor(
    private val viewModel: ExpensesChartViewModel
): DashboardPlugin {
    @Composable
    override fun ColumnScope.Render() {
        ExpensesChartScreen(viewModel = viewModel)
    }

    override fun refresh() {
        viewModel.refresh()
    }
}