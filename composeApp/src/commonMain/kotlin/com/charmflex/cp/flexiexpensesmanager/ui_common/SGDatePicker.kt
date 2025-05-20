package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun SGDatePicker(
    calendarState: CalendarState,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
    date: LocalDate?,
    isVisible: Boolean,
    boundary: ClosedRange<LocalDate>
) {
    if (isVisible) {
        VerticalCalendar(
            state = calendarState,
            dayContent = { day ->
                Day(day, boundary.contains(day.date), isSelected = date == day.date) { day ->
                    onConfirm(day.date)
                }
            }
        )
    }
}

@Composable
private fun Day(day: CalendarDay, isEnabled: Boolean, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = if (isSelected) Color.Green else Color.Transparent)
            .clickable(
                enabled = isEnabled,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.date.dayOfMonth.toString())
    }
}