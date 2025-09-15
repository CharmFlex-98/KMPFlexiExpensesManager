internal data class ProductInfo(
    val productId: String,
    val title: String,
    val description: String,
    val price: String,
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val type: ProductType
)

internal data class Purchase(
    val orderId: String,
    val packageName: String,
    val productId: String,
    val purchaseTime: Long,
    val purchaseToken: String,
    val isAcknowledged: Boolean
)

internal enum class ProductType {
    INAPP, SUBSCRIPTION
}

sealed class PurchaseResult {
    object Success : PurchaseResult()
    object UserCanceled : PurchaseResult()
    data class Error(val message: String) : PurchaseResult()
}