package com.charmflex.cp.flexiexpensesmanager.core.utils

import kotlin.math.pow

internal interface CurrencyFormatter {
    fun format(minorUnitAmount: Long, currencyCode: String): String
    fun formatWithoutSymbol(minorUnitAmount: Long, currencyCode: String): String
    fun formatTo(minorUnitAmount: Long, currencyCode: String, fromCurrencyCode: String, rate: Float): String
    fun from(amountValue: Double, currencyCode: String): Long
}

class CurrencyFormatterImpl : CurrencyFormatter {

    override fun format(minorUnitAmount: Long, currencyCode: String): String {
        val amount = minorToMajor(minorUnitAmount, currencyCode)
        val formatted = amount.toString() // basic 2 decimal formatting
        return "$currencyCode $formatted" // you can customize symbol if needed
    }

    override fun formatWithoutSymbol(minorUnitAmount: Long, currencyCode: String): String {
        return minorToMajor(minorUnitAmount, currencyCode).toString()
    }

    override fun formatTo(
        minorUnitAmount: Long,
        currencyCode: String,
        fromCurrencyCode: String,
        rate: Float
    ): String {
        val fromAmount = minorToMajor(minorUnitAmount, fromCurrencyCode)
        val converted = fromAmount * rate
        val formatted = converted.toString()
        return "$currencyCode $formatted"
    }

    override fun from(amountValue: Double, currencyCode: String): Long {
        val factor = getFactor(currencyCode)
        return (amountValue * factor).toLong()
    }

    private fun minorToMajor(minorUnitAmount: Long, currencyCode: String): Double {
        val factor = getFactor(currencyCode)
        return minorUnitAmount.toDouble() / factor
    }

    private fun getFactor(currencyCode: String): Double {
        // Basic hardcoded fraction digits fallback (customize as needed)
        val fractionDigits = when (currencyCode) {
            "JPY", "KRW" -> 0
            else -> 2
        }
        return 10.0.pow(fractionDigits)
    }
}
