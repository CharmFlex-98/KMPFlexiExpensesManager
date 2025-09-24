package com.charmflex.cp.flexiexpensesmanager.features.billing

import ProductInfo
import FEMPurchase
import PurchaseResult


internal interface BillingManager {
    suspend fun queryProducts(): List<ProductInfo>
    suspend fun purchaseProduct(productId: String): PurchaseResult
    suspend fun queryPurchases(): List<FEMPurchase>
    suspend fun consumePurchase(purchaseToken: String): Boolean
    fun cleanup()
}