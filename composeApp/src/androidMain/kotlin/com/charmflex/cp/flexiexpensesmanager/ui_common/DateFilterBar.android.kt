package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.FilterMenuDropDownItem
import com.charmflex.flexiexpensesmanager.ui_common.FECallout3
import com.charmflex.flexiexpensesmanager.ui_common.grid_x1
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import java.time.Month

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
    var dropDownExpanded by remember { mutableStateOf(false) }
    val datePickerUseCaseState = remember { UseCaseState() }
    var customDateSelection by remember { mutableStateOf<CustomDateSelection?>(null) }
    val selectedMenu = when (currentDateFilter) {
        is DateFilter.Monthly -> stringResource(id = FilterMenuDropDownItem.Monthly.titleResId)
        is DateFilter.Custom -> stringResource(id = FilterMenuDropDownItem.Custom.titleResId)
        is DateFilter.All -> stringResource(id = FilterMenuDropDownItem.All.titleResId)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (currentDateFilter) {
            is DateFilter.Monthly -> MonthlyDateSelection(
                type = currentDateFilter,
                onShowMonthFilter = onShowMonthFilter,
                onDateFilterChanged = onDateFilterChanged
            )

            is DateFilter.Custom -> CustomDateSelection(
                type = currentDateFilter,
                onShowCustomStartFilter = onShowCustomStartFilter,
                onShowCustomEndFilter = onShowCustomEndFilter,
                onStartDateBoxClicked = {
                    customDateSelection =
                        CustomDateSelection(dateFilter = currentDateFilter, isStartSelected = true)
                },
                onEndDateBoxClicked = {
                    customDateSelection =
                        CustomDateSelection(dateFilter = currentDateFilter, isStartSelected = false)
                }
            )

            else -> FECallout3(modifier = Modifier
                .padding(grid_x1), text = stringResource(id = R.string._date_filter_show_all_description)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        DateFilterMenuSelection(
            dateFilterConfig,
            menuName = selectedMenu,
            onMenuTap = { dropDownExpanded = true },
            onDismiss = { dropDownExpanded = false },
            dropDownExpanded = dropDownExpanded
        ) {
            onDateFilterChanged(it)
        }
    }

    SGDatePicker(
        useCaseState = datePickerUseCaseState,
        onDismiss = { customDateSelection = null },
        onConfirm = { res ->
            val localDate = LocalDate(res.year, res.monthValue, res.dayOfMonth)
            val updatedFilter = customDateSelection?.let {
                if (it.isStartSelected) it.dateFilter.copy(from = localDate)
                else it.dateFilter.copy(to = localDate)
            }
            updatedFilter?.let {
                onDateFilterChanged(it)
            }
            customDateSelection = null
        },
        date = customDateSelection?.let {
            if (it.isStartSelected) it.dateFilter.from
            else it.dateFilter.to
        },
        isVisible = customDateSelection != null,
        boundary = java.time.LocalDate.now().minusYears(10)..java.time.LocalDate.now()
    )
}


@Composable
fun SGDatePicker(
    useCaseState: UseCaseState,
    onDismiss: () -> Unit,
    onConfirm: (java.time.LocalDateTime) -> Unit,
    date: LocalDate?,
    isVisible: Boolean,
    boundary: ClosedRange<java.time.LocalDate>
) {
    CalendarDialog(
        state = useCaseState,
        selection = CalendarSelection.Date(
            withButtonView = true,
            selectedDate = date?.let { java.time.LocalDate.of(it.year, Month.of(it.monthNumber), it.dayOfMonth) },
            onNegativeClick = onDismiss,
            onSelectDate = {
                onConfirm(it.atStartOfDay())
            }
        ),
        config = CalendarConfig(monthSelection = true, yearSelection = true, boundary = boundary)
    )

    if (isVisible) useCaseState.show()
    else useCaseState.hide()
}