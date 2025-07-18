package com.charmflex.cp.flexiexpensesmanager.core.utils

import io.fluidsonic.currency.Currency
import kotlin.math.pow

internal interface CurrencyFormatter {
    fun format(minorUnitAmount: Long, currencyCode: String): String
    fun formatWithoutSymbol(minorUnitAmount: Long, currencyCode: String): String
    fun formatTo(minorUnitAmount: Long, currencyCode: String, fromCurrencyCode: String, rate: Float): String
    fun from(amountValue: Double, currencyCode: String): Long
}
