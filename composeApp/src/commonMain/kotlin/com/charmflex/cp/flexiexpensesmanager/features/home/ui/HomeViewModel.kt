package com.charmflex.cp.flexiexpensesmanager.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.SharedPrefConstant
import com.charmflex.cp.flexiexpensesmanager.features.billing.event.BillingEventName
import com.charmflex.cp.flexiexpensesmanager.features.currency.usecases.UpdateCurrencyRateUseCase
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
internal class HomeViewModel constructor(
    private val routeNavigator: RouteNavigator,
    private val updateCurrencyRateUseCase: UpdateCurrencyRateUseCase,
    private val scheduledTransactionHandler: ScheduledTransactionHandler,
    private val billingManager: BillingManager,
    private val eventTracker: EventTracker,
    private val sharedPrefs: SharedPrefs,
) : ViewModel() {
    private val _homeItemsRefreshable: MutableList<HomeItemRefreshable> = mutableListOf()

    init {
        viewModelScope.launch {
            updateCurrencyRateUseCase().fold(
                onSuccess = {
                },
                onFailure = {
                }
            )
        }

        viewModelScope.launch {
            scheduledTransactionHandler.update()
        }

        viewModelScope.launch {
            resultOf {
                billingManager.queryPurchases()
            }.onSuccess { purchases ->
                withContext(Dispatchers.IO) {
                    BillingConstant.ALL_PRODUCTS.forEach {
                        val product = purchases.firstOrNull { purchase -> purchase.productId == it }
                        val isPurchased = product?.isPurchased == true
                        sharedPrefs.setBoolean(SharedPrefConstant.PRODUCT_BOUGHT_PREFIX + it, isPurchased)
                    }
                }

                eventTracker.track(EventData.simpleEvent(BillingEventName.HOME_QUERY_PURCHASE_SUCCESS))
            }.onFailure {
                eventTracker.track(EventData.simpleEvent(BillingEventName.HOME_QUERY_PURCHASE_FAILED))
            }
        }
    }

    fun initHomeRefreshable(vararg items: HomeItemRefreshable) {
        _homeItemsRefreshable.addAll(items)
    }

    fun createNewExpenses() {
        routeNavigator.navigateTo(TransactionRoute.newTransactionDestination())
    }

    // Make sure only call this when needed
    fun notifyRefresh() {
        _homeItemsRefreshable.forEach {
            it.refreshHome()
        }
    }
}

internal interface HomeItemRefreshable {
    fun refreshHome()
}