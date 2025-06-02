package com.charmflex.cp.flexiexpensesmanager.core.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.round


inline fun <T> resultOf(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellation: CancellationException) {
        // Rethrow for cancellation exception because coroutine need to catch this for proper cancellation
        throw cancellation
    } catch (e: Throwable) {
        Result.failure(exception = e)
    }
}

fun <T> unwrapResult(result: Result<T>): T {
    return result.getOrThrow()
}

internal fun toPercentageString(ratio: Float): String {
    val percentValue = round(ratio * 100) / 100
    return percentValue.toString()
}

const val DEFAULT_DATE_TIME_PATTERN = "dd MMMM yyyy hh:mm a"
const val DATE_ONLY_DEFAULT_PATTERN = "yyyy-MM-dd"
const val MONTH_ONLY_DEFAULT_PATTERN = "MMMM"
const val YEAR_ONLY_DEFAULT_PATTERN = "yyyy"
const val MONTH_YEAR_PATTERN = "MMMM yyyy"
const val MONTH_YEAR_DB_PATTERN = "MMMM/yyyy"
const val SHORT_MONTH_YEAR_PATTERN = "MM-yyyy"
const val TIME_ONLY_DEFAULT_PATTERN = "hh:mm a"


// TO CLIENT
fun String.toLocalDateTime(pattern: String): LocalDateTime? {
    if (this.isEmpty()) return null
    return LocalDateTime.parse(this, getDateTimeFormatter(pattern))
}

fun String.toLocalDate(pattern: String): LocalDate? {
    if (this.isEmpty()) return null
    return LocalDate.parse(this, getDateFormatter(pattern))
}

fun LocalDate?.toStringWithPattern(pattern: String): String {
    return this?.format(getDateFormatter(pattern)) ?: ""
}

fun LocalDateTime?.toStringWithPattern(pattern: String): String {
    return this?.format(getDateTimeFormatter(pattern)) ?: ""
}

// INTERNAL

@OptIn(FormatStringsInDatetimeFormats::class)
private fun getDateTimeFormatter(pattern: String): DateTimeFormat<LocalDateTime> {
    return LocalDateTime.Format {
        byUnicodePattern(pattern)
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
private fun getDateFormatter(pattern: String): DateTimeFormat<LocalDate> {
    return LocalDate.Format {
        byUnicodePattern(pattern)
    }
}