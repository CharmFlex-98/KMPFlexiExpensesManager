package com.charmflex.cp.flexiexpensesmanager.features.billing.constant

object BillingConstant {
    // Product IDs for your app
    const val PREMIUM_MONTHLY = "flexi_premium_monthly"
    const val PREMIUM_YEARLY = "flexi_premium_yearly"
    const val REMOVE_ADS = "flexi_remove_ads"
    const val UNLOCK_REPORTS = "flexi_unlock_reports"

    // All product IDs
    val ALL_PRODUCTS = listOf(
        PREMIUM_MONTHLY,
        PREMIUM_YEARLY,
        REMOVE_ADS,
        UNLOCK_REPORTS
    )
}