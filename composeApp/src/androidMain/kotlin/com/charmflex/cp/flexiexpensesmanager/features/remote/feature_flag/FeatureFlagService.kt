package com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag
import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.app.AppFlavour
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature


internal class FeatureFlagServiceImpl(
    private val billingManager: BillingManager,
    private val appConfigProvider: AppConfigProvider
) : FeatureFlagService {
    override suspend fun isPremiumFeatureAllowed(premiumFeature: PremiumFeature): Boolean {
        if (appConfigProvider.getAppFlavour() == AppFlavour.PAID) {
            return true
        }

        return when (premiumFeature) {
            PremiumFeature.BUDGET, PremiumFeature.BACKUP, PremiumFeature.SCHEDULER -> {
                val purchases = billingManager.queryPurchases()
                purchases.firstOrNull { it.productId == BillingConstant.PRO_VERSION_1 } != null
            }
        }
    }
}