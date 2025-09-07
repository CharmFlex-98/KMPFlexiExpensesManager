package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.minusYears
import com.kizitonwose.calendar.core.plusMonths
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun SGDatePicker(
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    date: LocalDate?,
    isVisible: Boolean,
    boundary: ClosedRange<LocalDate>
) {
    val calendarState = rememberCalendarState(startMonth = YearMonth.now().minusYears(10), endMonth = YearMonth.now(), firstVisibleMonth = YearMonth.now())

    if (isVisible) {
        var selectedDate by remember(date) { mutableStateOf(date) }
        val coroutineScope = rememberCoroutineScope()

        // Scroll to current date when picker opens
//        LaunchedEffect(isVisible) {
//            if (isVisible && date != null) {
//                calendarState.animateScrollToMonth(date.yearMonth)
//            }
//        }

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Select Date",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Calendar with navigation
                    HorizontalCalendar(
                        state = calendarState,
                        monthHeader = { month ->
                            MonthHeader(
                                calendarState = calendarState,
                                calendarMonth = month,
                                onPreviousMonth = {
                                    coroutineScope.launch {
                                        calendarState.animateScrollToMonth(month.yearMonth.minusMonths(1))
                                    }
                                },
                                onNextMonth = {
                                    coroutineScope.launch {
                                        calendarState.animateScrollToMonth(month.yearMonth.plusMonths(1))
                                    }
                                }
                            )
                        },
                        dayContent = { day ->
                            Day(
                                day = day,
                                isEnabled = boundary.contains(day.date) && day.position == DayPosition.MonthDate,
                                isSelected = selectedDate == day.date
                            ) { clickedDay ->
                                selectedDate = clickedDay.date
                            }
                        }
                    )

                    // Action buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismiss
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextButton(
                            onClick = {
                                selectedDate?.let { onConfirm(it) }
                            },
                            enabled = selectedDate != null
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthHeader(
    calendarState: CalendarState,
    calendarMonth: CalendarMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column {
        // Month/Year header with navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth, enabled = calendarMonth.yearMonth != calendarState.startMonth) {
                SGIcons.ArrowBack()
            }

            Text(
                text = "${calendarMonth.yearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${calendarMonth.yearMonth.year}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = onNextMonth, enabled = calendarMonth.yearMonth != calendarState.endMonth) {
                SGIcons.NextArrow()
            }
        }

        // Days of week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            daysOfWeek().forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.name.take(3),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isEnabled: Boolean,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                }
            )
            .clickable(
                enabled = isEnabled,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                !isEnabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                isSelected -> MaterialTheme.colorScheme.onPrimary
                else -> MaterialTheme.colorScheme.onSurface
            },
            fontSize = 14.sp
        )
    }
}