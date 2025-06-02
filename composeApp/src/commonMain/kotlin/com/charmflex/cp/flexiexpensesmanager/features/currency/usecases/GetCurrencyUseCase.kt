package com.charmflex.cp.flexiexpensesmanager.features.currency.usecases

import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import org.koin.core.annotation.Factory

// Get user set primary and secondary currency and it's rate
@Factory
internal class GetCurrencyUseCase  constructor(
    private val userCurrencyRepository: UserCurrencyRepository,
    private val getCurrencyUseCase: GetCurrencyRateUseCase,
) {

    suspend fun secondary(): Result<List<CurrencyRate>> {
        return resultOf {
            val rates = userCurrencyRepository.getSecondaryCurrency()
            rates.mapNotNull { getCurrencyUseCase.getPrimaryCurrencyRate(it) }
        }
    }

    suspend fun primary(): Result<CurrencyRate?> {
        return resultOf {
            val rate = userCurrencyRepository.getPrimaryCurrency()
            getCurrencyUseCase.getPrimaryCurrencyRate(rate)
        }
    }
}