package com.charmflex.cp.flexiexpensesmanager.features.billing

import ProductInfo
import Purchase
import PurchaseResult
import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.ref.WeakReference

internal class AndroidBillingManager(
    context: Activity
) : BillingManager, PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null
    private var isConnected = false
    private var clientCallback: ((PurchaseResult) -> Unit)? = null

    private val contextRef: WeakReference<Activity> = WeakReference(context)

    override suspend fun initialize(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val context = contextRef.get()

            if (context == null) {
                continuation.resume(false) { _, _, _ -> }
                return@suspendCancellableCoroutine
            }

            billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .build()

            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    isConnected = billingResult.responseCode == BillingClient.BillingResponseCode.OK
                    continuation.resume(isConnected) { _, _, _ ->
                        billingClient?.endConnection()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    isConnected = false
                }
            })
        }
    }

    override suspend fun queryProducts(): List<ProductInfo> {
        return suspendCancellableCoroutine { continuation ->
            if (!isConnected) {
                continuation.resume(emptyList()) { _, _, _ -> }
                return@suspendCancellableCoroutine
            }

            val productList = BillingConstant.ALL_PRODUCTS.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val products = productDetailResult.productDetailsList.map { productDetails ->
                        ProductInfo(
                            productId = productDetails.productId,
                            title = productDetails.title,
                            description = productDetails.description,
                            price = productDetails.oneTimePurchaseOfferDetails?.formattedPrice
                                ?: "",
                            priceAmountMicros = productDetails.oneTimePurchaseOfferDetails?.priceAmountMicros
                                ?: 0L,
                            priceCurrencyCode = productDetails.oneTimePurchaseOfferDetails?.priceCurrencyCode
                                ?: "",
                            type = ProductType.INAPP
                        )
                    }
                    continuation.resume(products) { _, _, _ ->

                    }
                } else {
                    continuation.resume(emptyList()) { _, _, _ ->

                    }
                }
            }
        }
    }

    override suspend fun purchaseProduct(productId: String): PurchaseResult {
        return suspendCancellableCoroutine { continuation ->
            clientCallback = {
                continuation.resume(it) { _, _, _ -> }
            }

            val productDetailsParams = QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(listOf(productDetailsParams))
                .build()

            billingClient?.queryProductDetailsAsync(params) { billingResult, productDetailResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val productDetails = productDetailResult.productDetailsList
                    val purchaseParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(
                            productDetails.map {
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(it)
                                    .build()
                            }
                        )
                        .build()

                    val activity = contextRef.get()
                    if (activity == null) {
                        clientCallback?.invoke(PurchaseResult.Error("Activity not found"))
                        return@queryProductDetailsAsync
                    }

                    billingClient?.launchBillingFlow(activity, purchaseParams)
                } else {
                    clientCallback?.invoke(PurchaseResult.Error("Product not found"))
                }
            }
        }
    }

    override suspend fun queryPurchases(): List<Purchase> {
        return suspendCancellableCoroutine { continuation ->
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val purchaseList = purchases.map { purchase ->
                        Purchase(
                            orderId = purchase.orderId ?: "",
                            packageName = purchase.packageName,
                            productId = purchase.products.firstOrNull() ?: "",
                            purchaseTime = purchase.purchaseTime,
                            purchaseToken = purchase.purchaseToken,
                            isAcknowledged = purchase.isAcknowledged
                        )
                    }
                    continuation.resume(purchaseList) { _, _, _ -> }
                } else {
                    continuation.resume(emptyList()) { _, _, _ -> }
                }
            }
        }
    }

    override suspend fun consumePurchase(purchaseToken: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build()

            billingClient?.consumeAsync(consumeParams) { billingResult, _ ->
                continuation.resume(billingResult.responseCode == BillingClient.BillingResponseCode.OK) { _, _, _ -> }
            }
        }
    }

    override fun cleanup() {
        billingClient?.endConnection()
        billingClient = null
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<com.android.billingclient.api.Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                clientCallback?.invoke(PurchaseResult.Success)
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                clientCallback?.invoke(PurchaseResult.UserCanceled)
            }
            else -> {
                clientCallback?.invoke(PurchaseResult.Error("Purchase failed: ${billingResult.debugMessage}"))
            }
        }
    }
}