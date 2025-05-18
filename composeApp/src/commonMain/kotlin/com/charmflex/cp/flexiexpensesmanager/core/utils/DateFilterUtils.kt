package com.charmflex.cp.flexiexpensesmanager.core.utils

import android.annotation.SuppressLint
import android.os.Parcelable
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import com.charmflex.flexiexpensesmanager.core.utils.DATE_ONLY_DEFAULT_PATTERN
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
sealed interface DateFilter : Parcelable {

    @SuppressLint("ParcelCreator")
    object All : DateFilter

    @SuppressLint("ParcelCreator")
    data class Monthly(
        // If monthBefore = 0, means this month; if = 1, means last month and so on
        val monthBefore: Int
    ) : DateFilter

    @SuppressLint("ParcelCreator")
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
            val localDateNow = LocalDate.now()
            localDateNow.minusMonths(this.monthBefore).let {
                val res = it.withDayOfMonth(it.lengthOfMonth())
                if (res.isAfter(localDateNow)) localDateNow
                else res
            }.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
        }
        is DateFilter.Custom -> {
            val localDateNow = LocalDate.now()
            val res = if (this.to.isAfter(this.to)) localDateNow else this.to
            res.toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
        }
        else -> null
    }
}