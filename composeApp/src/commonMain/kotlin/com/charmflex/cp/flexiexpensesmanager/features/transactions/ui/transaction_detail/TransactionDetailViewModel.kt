package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail

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
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@org.koin.core.annotation.Factory
internal class TransactionDetailViewModelFactory  constructor(
    private val routeNavigator: RouteNavigator,
    private val transactionRepository: TransactionRepository,
    private val resourcesProvider: ResourcesProvider,
    private val currencyFormatter: CurrencyFormatter
) {
    fun create(transactionId: Long): TransactionDetailViewModel {
        return TransactionDetailViewModel(
            transactionId = transactionId,
            routeNavigator = routeNavigator,
            transactionRepository = transactionRepository,
            resourcesProvider = resourcesProvider,
            currencyFormatter = currencyFormatter
        )
    }
}
internal class TransactionDetailViewModel(
    private val transactionId: Long,
    private val routeNavigator: RouteNavigator,
    private val transactionRepository: TransactionRepository,
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
        toggleLoader(false)
        viewModelScope.launch {
            transactionRepository.getTransactionById(transactionId = transactionId)
                .catch {
                    toggleLoader(false)
                }
                .collectLatest { transaction ->
                    _viewState.update {
                        it.copy(
                            detail = TransactionDetailViewState.Detail(
                                transactionId = transaction.transactionId,
                                transactionName = transaction.transactionName,
                                transactionAccountFrom = transaction.transactionAccountFrom,
                                transactionAccountTo = transaction.transactionAccountTo,
                                transactionTypeCode = transaction.transactionTypeCode,
                                formattedAmount = currencyFormatter.format(
                                    transaction.minorUnitAmount,
                                    transaction.currency
                                ),
                                transactionDate = transaction.transactionDate,
                                transactionCategory = transaction.transactionCategory
                            )
                        )
                    }
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
                transactionRepository.deleteTransactionById(transactionId)
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

internal data class TransactionDetailViewState(
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
        val formattedAmount: String,
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