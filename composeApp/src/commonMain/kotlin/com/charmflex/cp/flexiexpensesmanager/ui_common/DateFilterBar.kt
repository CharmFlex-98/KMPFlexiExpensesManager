package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.charmflex.cp.flexiexpensesmanager.core.utils.DATE_ONLY_DEFAULT_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.SHORT_MONTH_YEAR_PATTERN
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.minusMonths
import com.charmflex.cp.flexiexpensesmanager.core.utils.toStringWithPattern
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.FilterMenuDropDownItem
import com.charmflex.flexiexpensesmanager.ui_common.FECallout3
import com.charmflex.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.flexiexpensesmanager.ui_common.grid_x0_25
import com.charmflex.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.flexiexpensesmanager.ui_common.grid_x12
import com.charmflex.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.flexiexpensesmanager.ui_common.grid_x3
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import kotlin.reflect.KClass

@Composable
expect fun DateFilterBar(
    modifier: Modifier = Modifier,
    currentDateFilter: DateFilter,
    onDateFilterChanged: (DateFilter) -> Unit,
    onShowMonthFilter: (LocalDate) -> String = {
        it.toStringWithPattern(SHORT_MONTH_YEAR_PATTERN)
    },
    onShowCustomStartFilter: (LocalDate) -> String = {
        it.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
    },
    onShowCustomEndFilter: (LocalDate) -> String = {
        it.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
    },
    dateFilterConfig: DateFilterConfig = DateFilterConfig()
)

@Composable
fun DateFilterMenuSelection(
    dateFilterConfig: DateFilterConfig,
    menuName: String,
    onMenuTap: () -> Unit,
    dropDownExpanded: Boolean,
    onDismiss: () -> Unit,
    onDateFilterChanged: (DateFilter) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {
                onMenuTap()
            }
            .border(BorderStroke(grid_x0_25, color = Color.Black))
            .padding(
                grid_x1
            )
    ) {
        FECallout3(text = menuName)
        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = onDismiss
        ) {
            dateFilterConfig.dateFilterOptions.mapNotNull {
                when (it) {
                    DateFilter.All::class -> FilterMenuDropDownItem.All
                    DateFilter.Monthly::class -> FilterMenuDropDownItem.Monthly
                    DateFilter.Custom::class -> FilterMenuDropDownItem.Custom
                    else -> null
                }
            }.forEach { item ->
                DropdownMenuItem(
                    text = {
                        FECallout3(text = stringResource(item.titleResId))
                    }, onClick = {
                        when (item.filterMenuItemType) {
                            FilterMenuDropDownItem.FilterMenuItemType.MONTHLY -> {
                                val newDateFilter = DateFilter.Monthly(0)
                                onDateFilterChanged(newDateFilter)
                            }

                            FilterMenuDropDownItem.FilterMenuItemType.CUSTOM -> {
                                val newDateFilter = DateFilter.Custom(
                                    from = localDateNow().let { LocalDate(it.year, it.monthNumber, 1) },
                                    to = localDateNow()
                                )
                                onDateFilterChanged(newDateFilter)
                            }

                            FilterMenuDropDownItem.FilterMenuItemType.ALL -> {
                                onDateFilterChanged(DateFilter.All)
                            }
                        }
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
fun MonthlyDateSelection(
    type: DateFilter.Monthly,
    onShowMonthFilter: (LocalDate) -> String,
    onDateFilterChanged: (DateFilter) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val text = onShowMonthFilter(localDateNow().minusMonths(type.monthBefore))
        IconButton(
            modifier = Modifier.height(grid_x3),
            onClick = {
                onDateFilterChanged(type.copy(monthBefore = type.monthBefore + 1))
            }
        ) {
            SGIcons.ArrowBack(modifier = Modifier.size(grid_x2))
        }
        FECallout3(
            modifier = Modifier
                .width(grid_x12)
                .padding(horizontal = grid_x1), text = text
        )
        if (type.monthBefore > 0) IconButton(
            modifier = Modifier.height(grid_x3),
            onClick = {
                onDateFilterChanged(type.copy(monthBefore = type.monthBefore - 1))
            }
        ) {
            SGIcons.NextArrow(modifier = Modifier.size(grid_x2))
        }
    }
}

@Composable
fun CustomDateSelection(
    type: DateFilter.Custom,
    onShowCustomStartFilter: (LocalDate) -> String,
    onShowCustomEndFilter: (LocalDate) -> String,
    onStartDateBoxClicked: () -> Unit,
    onEndDateBoxClicked: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        FECallout3(modifier = Modifier
            .clickable {
                onStartDateBoxClicked()
            }
            .padding(grid_x1), text = onShowCustomStartFilter(type.from))

        FECallout3(text = " ~ ")

        FECallout3(modifier = Modifier
            .clickable {
                onEndDateBoxClicked()
            }
            .padding(grid_x1), text = onShowCustomEndFilter(type.to))
    }
}


internal data class CustomDateSelection(
    val dateFilter: DateFilter.Custom,
    val isStartSelected: Boolean
)

data class DateFilterConfig (
    val dateFilterOptions: List<KClass<out DateFilter>> = listOf(DateFilter.All::class, DateFilter.Custom::class, DateFilter.Monthly::class)
)
