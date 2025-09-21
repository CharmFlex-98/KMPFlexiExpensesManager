package com.charmflex.cp.flexiexpensesmanager.core.utils.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.DurationUnit

internal fun localDateNow(): LocalDate {
    return getLocalDateTime().date
}

internal fun localDateTimeNow(): LocalDateTime {
    return getLocalDateTime()
}

fun getLocalDateTime(): LocalDateTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
}

internal fun LocalDate.isAfter(localDate: LocalDate): Boolean {
    return this > localDate
}

internal fun LocalDate.minusMonths(months: Int): LocalDate {
    return this.minus(months, DateTimeUnit.MONTH)
}

internal fun LocalDate.minusYears(years: Int): LocalDate {
    return this.minus(years, DateTimeUnit.YEAR)
}

internal fun LocalDate.plusMonths(value: Int): LocalDate = plus(value, DateTimeUnit.MONTH)

fun LocalDate.plusDays(value: Int): LocalDate = plus(value, DateTimeUnit.DAY)

fun LocalDate.plusYears(value: Int): LocalDate = plus(value, DateTimeUnit.YEAR)



/**
 * Minus this by from
 */
internal fun LocalDateTime.getHourDifference(from: LocalDateTime): Long {
    val fromInstant = from.toInstant(TimeZone.currentSystemDefault())
    val toInstant = this.toInstant(TimeZone.currentSystemDefault())

    val duration: Duration = toInstant - fromInstant
    return duration.toLong(DurationUnit.HOURS)
}