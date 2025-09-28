package com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag
import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppFlavor
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.SharedPrefConstant
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature


internal class FeatureFlagServiceImpl(
    private val billingManager: BillingManager,
    private val appConfigProvider: AppConfigProvider,
    private val sharedPrefs: SharedPrefs
) : FeatureFlagService {
    override suspend fun isPremiumFeatureAllowed(premiumFeature: PremiumFeature): Result<Boolean> {
        return resultOf {
            if (appConfigProvider.getAppFlavour() == AppFlavor.PAID) {
                true
            } else {
                when (premiumFeature) {
                    PremiumFeature.BUDGET, PremiumFeature.BACKUP, PremiumFeature.SCHEDULER -> {
                        val cachedPurchased = sharedPrefs.getBoolean(SharedPrefConstant.PRODUCT_BOUGHT_PREFIX + BillingConstant.PRO_VERSION_1, false)
                        if (cachedPurchased) {
                            true
                        } else {
                            val purchases = billingManager.queryPurchases()
                            purchases.firstOrNull { it.productId == BillingConstant.PRO_VERSION_1 && it.isPurchased} != null
                        }
                    }
                }
            }
        }

    }
}