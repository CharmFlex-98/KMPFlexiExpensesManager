package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SGTimePicker(
//    timePickerState: UseCaseState,
//    onDismiss: () -> Unit,
//    onConfirm: (hour: Int, min: Int) -> Unit,
//    isVisible: Boolean
//) {
//
//    ClockDialog(
//        state = timePickerState,
//        selection = ClockSelection.HoursMinutes(
//            onNegativeClick = onDismiss,
//            onPositiveClick = { h, m ->
//                onConfirm(h, m)
//            }
//        ),
//        config = ClockConfig(is24HourFormat = true)
//    )
//
//    if (isVisible) timePickerState.show()
//    else timePickerState.hide()
//}