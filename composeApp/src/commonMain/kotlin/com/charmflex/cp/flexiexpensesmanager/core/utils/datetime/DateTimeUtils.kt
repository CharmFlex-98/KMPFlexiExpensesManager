package com.charmflex.cp.flexiexpensesmanager.core.utils.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

internal fun localDateNow(): LocalDate {
    return getLocalDateTime().date
}

internal fun localDateTimeNow(): LocalDateTime {
    return getLocalDateTime()
}

private fun getLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

internal fun LocalDate.isAfter(localDate: LocalDate): Boolean {
    return this > localDate
}

internal fun LocalDate.minusMonths(months: Int): LocalDate {
    return this.minus(DatePeriod(months = months))
}