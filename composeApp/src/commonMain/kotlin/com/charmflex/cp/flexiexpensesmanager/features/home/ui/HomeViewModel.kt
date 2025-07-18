package com.charmflex.cp.flexiexpensesmanager.features.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.features.currency.usecases.UpdateCurrencyRateUseCase
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ScheduledTransactionHandler
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class HomeViewModel constructor(
    private val routeNavigator: RouteNavigator,
    private val updateCurrencyRateUseCase: UpdateCurrencyRateUseCase,
    private val scheduledTransactionHandler: ScheduledTransactionHandler
) : ViewModel() {
    private val _homeItemsRefreshable: MutableList<HomeItemRefreshable> = mutableListOf()

    init {
        println("Success homeviewmodel")
        viewModelScope.launch {
            updateCurrencyRateUseCase().fold(
                onSuccess = {
                    println("Success")
                },
                onFailure = {
                    println(it.message)
                }
            )
        }

        viewModelScope.launch {
            scheduledTransactionHandler.update()
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