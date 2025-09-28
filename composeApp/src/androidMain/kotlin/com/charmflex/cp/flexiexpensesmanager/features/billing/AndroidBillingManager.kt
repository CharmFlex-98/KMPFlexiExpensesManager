package com.charmflex.cp.flexiexpensesmanager.features.billing

import ProductInfo
import FEMPurchase
import FEMPurchaseState
import PurchaseResult
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.charmflex.cp.flexiexpensesmanager.core.app.AndroidAppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.app.model.AppFlavor
import com.charmflex.cp.flexiexpensesmanager.di.ActivityProvider
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.exceptions.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

internal class AndroidBillingManager(
    private val activityProvider: ActivityProvider,
    private val appConfigProvider: AndroidAppConfigProvider
) : BillingManager, PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null
    private var isConnected = false
    private var clientCallback: ((PurchaseResult) -> Unit)? = null
    private val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch {
            initialize()
        }
    }

    private suspend fun initialize(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val context = activityProvider.currentActivity
            if (context == null) {
                continuation.resume(false) { _, _, _ -> }
                return@suspendCancellableCoroutine
            }

            billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases(
                    PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
                )
                .enableAutoServiceReconnection()
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

            val productListCode = when (appConfigProvider.getAppFlavour()) {
                AppFlavor.FREE -> BillingConstant.ALL_PRODUCTS
                AppFlavor.PAID -> BillingConstant.PAID_FLAVOUR_PRODUCTS
                null -> BillingConstant.ALL_PRODUCTS
            }

            if (productListCode.isEmpty()) {
                continuation.resume(emptyList()) {_, _, _ -> }
                return@suspendCancellableCoroutine
            }

            val productList = productListCode.map { productId ->
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

                    val activity = activityProvider.currentActivity
                    if (activity == null) {
                        clientCallback?.invoke(PurchaseResult.Error("Activity not found"))
                        return@queryProductDetailsAsync
                    }

                    billingClient?.launchBillingFlow(activity, purchaseParams)
                } else {
                    clientCallback?.invoke(PurchaseResult.Error(billingResult.responseCode.toString()))
                }
            }
        }
    }

    override suspend fun queryPurchases(): List<FEMPurchase> {
        return suspendCancellableCoroutine { continuation ->
            val params = QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP)
                .build()

            billingClient?.queryPurchasesAsync(params) { billingResult, purchases ->
                when (billingResult.responseCode) {
                    BillingClient.BillingResponseCode.OK -> {
                        val fEMPurchaseList = purchases.map { purchase ->
                            FEMPurchase(
                                orderId = purchase.orderId ?: "",
                                packageName = purchase.packageName,
                                productId = purchase.products.firstOrNull() ?: "",
                                purchaseTime = purchase.purchaseTime,
                                purchaseToken = purchase.purchaseToken,
                                isAcknowledged = purchase.isAcknowledged,
                                isPurchased = purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                            )
                        }
                        continuation.resume(fEMPurchaseList) { _, _, _ -> }
                    }

                    BillingClient.BillingResponseCode.NETWORK_ERROR, BillingClient.BillingResponseCode.SERVICE_DISCONNECTED, BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                        continuation.resumeWithException(NetworkError)
                    }

                    else -> {
                        continuation.resume(emptyList()) { _, _, _ -> }
                    }
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
        purchaseList: MutableList<Purchase>?
    ) {
        clientCallback?.let {
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    if (purchaseList.isNullOrEmpty()) {
                        it.invoke(PurchaseResult.Error("No purchase is made."))
                        clientCallback = null
                        return
                    }
                    val purchaseState = when (purchaseList[0].purchaseState) {
                        Purchase.PurchaseState.PURCHASED -> FEMPurchaseState.PURCHASED
                        Purchase.PurchaseState.PENDING -> FEMPurchaseState.PENDING
                        else -> FEMPurchaseState.UNKNOWN
                    }
                    it.invoke(PurchaseResult.Success(purchaseState))
                    clientCallback = null
                }

                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    it.invoke(PurchaseResult.UserCanceled)
                    clientCallback = null
                }

                else -> {
                    it.invoke(PurchaseResult.Error("Purchase failed: ${billingResult.debugMessage}"))
                    clientCallback = null
                }
            }
        }
    }
}