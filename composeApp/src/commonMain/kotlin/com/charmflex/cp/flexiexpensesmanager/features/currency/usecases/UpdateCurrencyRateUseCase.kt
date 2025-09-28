package com.charmflex.cp.flexiexpensesmanager.features.currency.usecases

import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.getHourDifference
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateTimeNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.CurrencyRepository
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

// This is for updating the currency rate (as source of truth for real time currency rate)
@Factory
internal class UpdateCurrencyRateUseCase(
    private val currencyRepository: CurrencyRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        return resultOf {
            val lastUpdate = currencyRepository.getLastCurrencyRateUpdateTimestamp()
            if (lastUpdate == null) {
                currencyRepository.fetchLatestCurrencyRates()
            } else {
                // Only update after 24 hours
                if (localDateTimeNow().getHourDifference(lastUpdate) > 24) {
                    currencyRepository.fetchLatestCurrencyRates()
                }
            }
        }
    }
}