package com.charmflex.cp.flexiexpensesmanager.core.utils

import java.time.LocalDate
import java.util.Date

fun Date.toLocalDate(): LocalDate {
    return this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
}