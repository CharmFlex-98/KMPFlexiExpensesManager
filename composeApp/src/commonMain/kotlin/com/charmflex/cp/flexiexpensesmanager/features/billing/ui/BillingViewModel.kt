package com.charmflex.cp.flexiexpensesmanager.features.billing.ui

import ProductInfo
import Purchase
import PurchaseResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.SharedPrefConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.event.BillingEventName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class BillingViewModel(
    private val billingManager: BillingManager,
    private val eventTracker: EventTracker,
    private val sharedPrefs: SharedPrefs
) : ViewModel() {
    private val _viewState = MutableStateFlow(BillingViewState())
    val viewState = _viewState.asStateFlow()

    var snackBarState = mutableStateOf("")
        private set

    fun init() {
        viewModelScope.launch {
            val products = billingManager.queryProducts()
            _viewState.update {
                it.copy(
                    productInfos = products
                )
            }
        }
    }

    fun purchaseProduct(productId: String) {
        viewModelScope.launch {
            val result = billingManager.purchaseProduct(productId)
            when (result) {
                PurchaseResult.Success -> {
                    sharedPrefs.setBoolean(SharedPrefConstant.PRODUCT_BOUGHT_PREFIX + productId, true)
                    eventTracker.track(EventData.simpleEvent(BillingEventName.PURCHASE_PRODUCT_SUCCESS + productId))
                }
                is PurchaseResult.Error -> {
                    snackBarState.value = result.message
                    eventTracker.track(EventData.simpleEvent(BillingEventName.PURCHASE_PRODUCT_FAILED + productId))
                }

                PurchaseResult.UserCanceled -> {
                    snackBarState.value = "Purchase is cancelled"
                    eventTracker.track(EventData.simpleEvent(BillingEventName.PURCHASE_PRODUCT_FAILED + productId))
                }

                else -> {}
            }
        }
    }

    fun resetState() {
        snackBarState.value = ""
    }
}

internal data class BillingViewState(
    val productInfos: List<ProductInfo> = listOf()
)