package com.charmflex.cp.flexiexpensesmanager.core.utils.datetime

import kotlinx.datetime.LocalDateTime
import java.time.LocalDate
import java.util.Date

fun Date.toLocalDate(): kotlinx.datetime.LocalDate {
    val data = this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
    return kotlinx.datetime.LocalDate(data.year, data.month, data.dayOfMonth)
}