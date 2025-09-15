package com.charmflex.cp.flexiexpensesmanager.features.billing

import ProductInfo
import Purchase
import PurchaseResult


internal interface BillingManager {
    suspend fun initialize(): Boolean
    suspend fun queryProducts(): List<ProductInfo>
    suspend fun purchaseProduct(productId: String): PurchaseResult
    suspend fun queryPurchases(): List<Purchase>
    suspend fun consumePurchase(purchaseToken: String): Boolean
    fun cleanup()
}