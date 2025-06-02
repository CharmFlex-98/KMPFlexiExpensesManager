package com.charmflex.cp.flexiexpensesmanager.core.utils
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import androidx.savedstate.write
import coil3.Uri
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val DateFilterNavType = object : NavType<DateFilter>(isNullableAllowed = true) {
    override fun get(bundle: SavedState, key: String): DateFilter? {
        return bundle.read { decodeFromSavedState(getSavedState(key)) as? DateFilter }
    }

    override fun serializeAsValue(value: DateFilter): String {
        return Json.encodeToString(value)
    }

    override fun parseValue(value: String): DateFilter {
        return Json.decodeFromString<DateFilter>(value)
    }

    override fun put(bundle: SavedState, key: String, value: DateFilter) {
        bundle.write { putSavedState(key, encodeToSavedState(value)) }
    }

}
@Serializable
sealed interface DateFilter {

    @Serializable
    object All : DateFilter

    @Serializable
    data class Monthly(
        // If monthBefore = 0, means this month; if = 1, means last month and so on
        @Serializable
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