package com.charmflex.cp.flexiexpensesmanager.features.currency.data.models

import androidx.room.ColumnInfo

internal data class UserCurrencyRateResponse(
    @ColumnInfo("rate")
    val rate: Float
)