package com.charmflex.cp.flexiexpensesmanager.features.billing.di

import com.charmflex.cp.flexiexpensesmanager.features.billing.ui.BillingViewModel

internal interface BillingInjector {
    fun billingViewModel(): BillingViewModel
}