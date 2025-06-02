package com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.koin.core.annotation.Factory

@Factory
internal class SchedulerListViewModel  constructor(
    private val schedulerRepository: TransactionSchedulerRepository,
    private val routeNavigator: RouteNavigator,
    private val currencyFormatter: CurrencyFormatter,
    private val resourcesProvider: ResourcesProvider
) : ViewModel() {
    private val _viewState = MutableStateFlow(SchedulerListViewState())
    val viewState = _viewState.asStateFlow()

    init {
        observeSchedulerList()
    }

    private fun observeSchedulerList() {
        viewModelScope.launch {
            schedulerRepository.getAllTransactionSchedulers().collectLatest { res ->
                _viewState.update {
                    it.copy(
                        schedulerItems = res.map {
                            ScheduledTransactionUIItem(
                                id = it.id.toLong(),
                                name = it.transactionName,
                                amount = currencyFormatter.format(it.minorUnitAmount, it.currency),
                                category = it.category?.name ?: kotlin.run {
                                    val value = if (it.transactionType == TransactionType.TRANSFER) Res.string.generic_transfer_capital else Res.string.generic_unknown_capital
                                    resourcesProvider.getString(value)
                                },
                                type = it.transactionType
                            )
                        }
                    )
                }
            }
            resultOf {
            }.fold(
                onSuccess = { res ->

                },
                onFailure = {}
            )
        }
    }

    fun addScheduler() {
        routeNavigator.navigateTo(TransactionRoute.newScheduleTransactionDestination())
        // Mocking now only
//        val currentDate = LocalDate.now()
//            .toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
//        viewModelScope.launch {
//            resultOf {
//                schedulerRepository.addNewExpensesScheduler(
//                    name = "Testing3 hour",
//                    fromAccountId = 2,
//                    amount = 10000,
//                    categoryId = 3,
//                    startDate = currentDate,
//                    currency = "MYR",
//                    tagIds = listOf(),
//                    rate = 1f,
//                    schedulerPeriod = SchedulerPeriod.MONTHLY,
//                )
//            }.fold(
//                onSuccess = {
//                    getSchedulers()
//                },
//                onFailure = {
//                    Log.d("test", it.toString())
//                }
//            )
//
//        }
    }

    fun removeScheduler(id: Int) {
        viewModelScope.launch {
            schedulerRepository.removeSchedulerById(id)
        }
    }
}

internal data class SchedulerListViewState(
    val schedulerItems: List<ScheduledTransactionUIItem> = listOf()
)

internal data class ScheduledTransactionUIItem(
    val id: Long,
    val name: String,
    val amount: String,
    val category: String,
    val type: TransactionType,
) {
    val iconResId: DrawableResource?
        get() {
            return when (type) {
                TransactionType.EXPENSES -> Res.drawable.ic_spend
                TransactionType.TRANSFER -> Res.drawable.ic_transfer_icon
                TransactionType.INCOME -> Res.drawable.ic_income
                else -> null
            }
        }
}