package com.charmflex.cp.flexiexpensesmanager.features.currency.data.models


data class CurrencyRateResponse(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Long,
    val date: String,
    val base: String,
    val rates: Map<String, Double>
)