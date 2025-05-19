package com.charmflex.cp.flexiexpensesmanager.core.utils
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable

@Serializable
sealed interface DateFilter {

    @Serializable
    object All : DateFilter

    @Serializable
    data class Monthly(
        // If monthBefore = 0, means this month; if = 1, means last month and so on
        val monthBefore: Int
    ) : DateFilter

    @Serializable
    data class Custom(
        val from: LocalDate,
        val to: LocalDate
    ) : DateFilter
}


internal fun DateFilter?.getStartDate(): String? {
    return  when (this) {
        is DateFilter.Monthly -> {
            val newLocalDate = localDateNow().minus(DatePeriod(months = 1))
            val res = LocalDate(newLocalDate.year, newLocalDate.month, 1)
            LocalDate(newLocalDate.year, newLocalDate.month, 1).toStringWithPattern(
                DATE_ONLY_DEFAULT_PATTERN
            )
        }
        is DateFilter.Custom -> {
            this.from.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
        }
        else -> null
    }
}

internal fun DateFilter?.getEndDate(): String? {
    return  when (this) {
        is DateFilter.Monthly -> {
            val localDateNow = localDateNow()
            val newLocalDate = localDateNow.minus(DatePeriod(months = this.monthBefore))
            LocalDate(newLocalDate.year, newLocalDate.month, newLocalDate.dayOfMonth).let {
                val res = it.lastDayOfMonth()
                if (res > localDateNow) localDateNow
                else res
            }.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
        }
        is DateFilter.Custom -> {
            val localDateNow = localDateNow()
            val res = if (this.to > localDateNow) localDateNow else this.to
            res.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
        }
        else -> null
    }
}

private fun LocalDate.lastDayOfMonth(): LocalDate {
    val nextMonth = if (this.monthNumber == 12) {
        LocalDate(this.year + 1, 1, 1)
    } else {
        LocalDate(this.year, this.monthNumber + 1, 1)
    }
    return nextMonth.minus(DatePeriod(days = 1))
}