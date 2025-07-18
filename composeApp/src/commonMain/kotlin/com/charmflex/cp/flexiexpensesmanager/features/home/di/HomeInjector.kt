package com.charmflex.cp.flexiexpensesmanager.features.home.di

import com.charmflex.cp.flexiexpensesmanager.features.home.ui.HomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.account.AccountHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard.DashboardViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.history.TransactionHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting.SettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_heat_map.ExpensesHeatMapViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.ExpensesChartViewModel

internal interface HomeInjector {
    fun homeViewModel(): HomeViewModel
    fun dashBoardViewModel(): DashboardViewModel
    fun expensesPieChartViewModel(): ExpensesChartViewModel
    fun expensesHeatMapViewModel(): ExpensesHeatMapViewModel
    fun expensesHistoryViewModel(): TransactionHomeViewModel
    fun accountHomeViewModel(): AccountHomeViewModel
    fun settingViewModel(): SettingViewModel
}