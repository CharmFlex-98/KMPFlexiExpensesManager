package com.charmflex.cp.flexiexpensesmanager.features.currency.di

import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.CurrencySettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.UserCurrencyViewModel

internal interface CurrencyInjector {
    fun currencySettingViewModel(): CurrencySettingViewModel
    fun userCurrencyViewModel(): UserCurrencyViewModel
}