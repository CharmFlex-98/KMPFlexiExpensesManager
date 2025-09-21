package com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TransactionRoute
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.account.domain.model.AccountGroup
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.ScheduledTransaction
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.TransactionDetailViewModel
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.TransactionDetailViewState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@org.koin.core.annotation.Factory
internal class SchedulerDetailViewModelFactory  constructor(
    private val routeNavigator: RouteNavigator,
    private val schedulerDetailRepository: TransactionSchedulerRepository,
    private val resourcesProvider: ResourcesProvider,
    private val currencyFormatter: CurrencyFormatter
) {
    fun create(transactionId: Long): SchedulerDetailViewModel {
        return SchedulerDetailViewModel(
            transactionId = transactionId,
            routeNavigator = routeNavigator,
            schedulerRepository = schedulerDetailRepository,
            resourcesProvider = resourcesProvider,
            currencyFormatter = currencyFormatter
        )
    }
}
internal class SchedulerDetailViewModel(
    private val transactionId: Long,
    private val routeNavigator: RouteNavigator,
    private val schedulerRepository: TransactionSchedulerRepository,
    private val resourcesProvider: ResourcesProvider,
    private val currencyFormatter: CurrencyFormatter
) : ViewModel() {

    var snackBarState: MutableState<SnackBarState> = mutableStateOf(SnackBarState.None)
        private set
    private val _viewState = MutableStateFlow(TransactionDetailViewState())
    val viewState = _viewState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            toggleLoader(true)
            try {
                val transaction: ScheduledTransaction = schedulerRepository.getTransactionSchedulerById(id = transactionId)
                    ?: return@launch

                _viewState.update {
                    val transactionAccount = if (transaction.transactionType == TransactionType.EXPENSES) {
                        transaction.accountFrom
                    } else transaction.accountTo

                    it.copy(
                        detail = TransactionDetailViewState.Detail(
                            transactionId = transaction.id.toLong(),
                            transactionName = transaction.transactionName,
                            transactionAccountFrom = transaction.accountFrom,
                            transactionAccountTo = transaction.accountTo,
                            transactionTypeCode = transaction.transactionType.toString(),
                            formattedTransactionAmount = currencyFormatter.format(
                                transaction.minorUnitAmount,
                                transaction.currency
                            ),
                            formattedAccountTransactionAmount = transactionAccount?.let { account ->
                                currencyFormatter.format(
                                    transaction.accountMinorUnitAmount,
                                    account.currency
                                )
                            } ?: "",
                            transactionDate = "start: ${transaction.startUpdateDate} (${
                                transaction.schedulerPeriod.toString().lowercase()
                            }) \nnext: ${transaction.nextUpdateDate}",
                            transactionCategory = transaction.category,
                        )
                    )
                }
                toggleLoader(false)
            } catch (e: Exception) {
                snackBarState.value = SnackBarState.Error(
                    message = resourcesProvider.getString(Res.string.generic_something_went_wron)
                )
                toggleLoader(false)
            }
        }
    }

    fun openDeleteWarningDialog() {
        _viewState.update {
            it.copy(
                dialogState = TransactionDetailViewState.DeleteDialogState
            )
        }
    }

    fun navigateToTransactionEdit() {
        routeNavigator.navigateTo(TransactionRoute.editTransactionDestination(transactionId))
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            resultOf {
                schedulerRepository.removeSchedulerById(transactionId.toInt())
            }.fold(
                onSuccess = {
                    _viewState.update {
                        it.copy(
                            dialogState = TransactionDetailViewState.SuccessDialog(
                                title = resourcesProvider.getString(Res.string.generic_success),
                                subtitle = resourcesProvider.getString(Res.string.delete_transaction_success_subtitle)
                            )
                        )
                    }
                },
                onFailure = {
                    snackBarState.value = SnackBarState.Error(
                        message = resourcesProvider.getString(Res.string.generic_something_went_wron)
                    )
                }
            )
        }
    }

    private fun toggleLoader(toggle: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = toggle
            )
        }
    }

    fun onCloseDialog() {
        _viewState.update {
            it.copy(
                dialogState = null
            )
        }
    }

    fun onBack() {
        routeNavigator.pop()
    }
}

internal data class SchedulerDetailViewState(
    val detail: Detail? = null,
    val isLoading: Boolean = false,
    val dialogState: DialogState? = null
) {
    data class Detail(
        val transactionId: Long,
        val transactionName: String,
        val transactionAccountFrom: AccountGroup.Account?,
        val transactionAccountTo: AccountGroup.Account?,
        val transactionTypeCode: String,
        val formattedTransactionAmount: String,
        val formattedAccountTransactionAmount: String,
        val transactionDate: String,
        val transactionCategory: Transaction.TransactionCategory?,
    ) {
        val allowEdit: Boolean get() = transactionTypeCode != TransactionType.UPDATE_ACCOUNT.name
    }

    sealed interface DialogState
    object DeleteDialogState : DialogState
    data class SuccessDialog(
        val title: String,
        val subtitle: String
    ) : DialogState
}