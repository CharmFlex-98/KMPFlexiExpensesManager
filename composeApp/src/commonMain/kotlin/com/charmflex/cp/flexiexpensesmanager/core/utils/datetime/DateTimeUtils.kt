package com.charmflex.cp.flexiexpensesmanager.core.utils.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
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