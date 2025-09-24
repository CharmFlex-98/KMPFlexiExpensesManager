internal data class ProductInfo(
    val productId: String,
    val title: String,
    val description: String,
    val price: String,
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val type: ProductType
) {
    companion object {
        fun empty() = ProductInfo(
            productId = "",
            title = "",
            description = "",
            price = "",
            priceAmountMicros = 0,
            priceCurrencyCode = "",
            type = ProductType.INAPP
        )
    }
}

internal data class FEMPurchase(
    val orderId: String,
    val packageName: String,
    val productId: String,
    val purchaseTime: Long,
    val purchaseToken: String,
    val isAcknowledged: Boolean,
    val isPurchased: Boolean
)

internal enum class ProductType {
    INAPP, SUBSCRIPTION
}

sealed class PurchaseResult {
    data class Success(
        val purchaseState: FEMPurchaseState
    ) : PurchaseResult()
    object UserCanceled : PurchaseResult()
    data class Error(val message: String) : PurchaseResult()
}

enum class FEMPurchaseState {
    PURCHASED, PENDING, UNKNOWN
}