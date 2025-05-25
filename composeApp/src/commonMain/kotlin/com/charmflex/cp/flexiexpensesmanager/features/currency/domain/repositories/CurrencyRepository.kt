package com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories

import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.models.CurrencyData
import kotlinx.datetime.LocalDateTime

internal interface CurrencyRepository {

    suspend fun fetchLatestCurrencyRates(): CurrencyData

    suspend fun getCacheCurrencyRates(): CurrencyData?

    suspend fun setLastCurrencyRateUpdateTimestamp(localDateTime: LocalDateTime)
    suspend fun getLastCurrencyRateUpdateTimestamp(): LocalDateTime?
}