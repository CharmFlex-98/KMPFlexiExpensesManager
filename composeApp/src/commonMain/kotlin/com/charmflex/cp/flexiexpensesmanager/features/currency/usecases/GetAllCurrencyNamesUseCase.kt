package com.charmflex.cp.flexiexpensesmanager.features.currency.usecases

import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.CurrencyRepository

internal class GetAllCurrencyNamesUseCase constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend operator fun invoke(): Result<List<String>> {
        return resultOf {
            currencyRepository.getCacheCurrencyRates()?.currencyRates?.map { it.key } ?: listOf()
        }
    }
}