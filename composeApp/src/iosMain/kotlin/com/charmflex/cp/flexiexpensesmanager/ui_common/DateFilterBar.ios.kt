package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import kotlinx.datetime.LocalDate

@Composable
actual fun DateFilterBar(
    modifier: Modifier,
    currentDateFilter: DateFilter,
    onDateFilterChanged: (DateFilter) -> Unit,
    onShowMonthFilter: (LocalDate) -> String,
    onShowCustomStartFilter: (LocalDate) -> String,
    onShowCustomEndFilter: (LocalDate) -> String,
    dateFilterConfig: DateFilterConfig
) {
}