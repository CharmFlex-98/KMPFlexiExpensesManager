package com.charmflex.cp.flexiexpensesmanager.core.utils

internal interface RateExchangeManager {
    fun convertTo(minorUnitAmount: Long, currencyCode: String, fromCurrencyCode: String, rate: Float): String
    fun getRate(fromCurrency: String, minorUnitAmountFrom: Long, toCurrency: String, minorUnitAmountTo: Long): Float
}