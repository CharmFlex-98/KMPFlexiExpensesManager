package com.charmflex.cp.flexiexpensesmanager.features.billing.ui

import ProductInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class BillingViewModel(
    private val billingManager: BillingManager
) : ViewModel() {
    private val _viewState = MutableStateFlow(BillingViewState())
    val viewState = _viewState.asStateFlow()

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
                    println("Success")
                }
                else -> {
                    println("Failed to purhcase")
                }
            }
        }
    }
}

internal data class BillingViewState(
    val productInfos: List<ProductInfo> = listOf()
)