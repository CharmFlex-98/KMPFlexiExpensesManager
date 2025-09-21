package com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag

import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature

internal interface FeatureFlagService {
    suspend fun isPremiumFeatureAllowed(premiumFeature: PremiumFeature): Result<Boolean>
}