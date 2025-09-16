package com.charmflex.cp.flexiexpensesmanager.features.backup.ui

import AccountRepository
import BillingRoutes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AccountRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TagRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateTimeNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.backup.TransactionBackupManager
import com.charmflex.cp.flexiexpensesmanager.features.backup.checker.ImportDataChecker
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.constant.BillingConstant
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.FeatureFlagService
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionDomainInput
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class ImportDataViewModel constructor(
    private val backupManager: TransactionBackupManager,
    private val fileProvider: DocumentManager,
    private val importDataChecker: ImportDataChecker,
    private val routeNavigator: RouteNavigator,
    private val transactionTagRepository: TransactionRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val userCurrencyRepository: UserCurrencyRepository,
    private val accountRepository: AccountRepository,
    private val featureFlagService: FeatureFlagService
) : ViewModel() {
    private val _viewState = MutableStateFlow(ImportDataViewState())
    val viewState = _viewState.asStateFlow()

    private val _tabIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val tabIndex = _tabIndex.asStateFlow()

    private var awaitingMissingData: ImportedData.MissingData? = null

    private val _snackbarState = MutableStateFlow("")
    val snackBarState = _snackbarState.asStateFlow()


    init {
        viewModelScope.launch {
            val isFeatureEnabled = featureFlagService.isPremiumFeatureAllowed(PremiumFeature.BACKUP)
            _viewState.update {
                it.copy(
                    isFeatureEnabled = isFeatureEnabled
                )
            }
        }
        viewModelScope.launch {
            backupManager.progress.collectLatest { progress ->
                _viewState.update {
                    it.copy(
                        progress = progress
                    )
                }
            }
        }
    }

    fun purchaseIAP() {
        routeNavigator.navigateTo(BillingRoutes.Root)
    }

    fun updateTabIndex(tabIndex: Int) {
        _tabIndex.value = tabIndex
    }

    fun updateImportedData() {
        viewModelScope.launch {
            val (newImportedData, newMissingData) = importDataChecker.updateRequiredData(
                _viewState.value.missingData,
                _viewState.value.importedData
            )
            _viewState.update {
                it.copy(
                    importedData = newImportedData,
                    missingData = newMissingData
                )
            }
        }
    }

    fun importData(importedDocument: ByteArray?) {
        if (importedDocument == null) {
            // TODO: Error snackbar
            return
        }
        viewModelScope.launch {
            toggleLoader(true)
            val currentTime = localDateTimeNow()
            val fileName = "cache_import_file_${currentTime}"
            fileProvider.writeCacheFile(importedDocument, fileName)
            resultOf {
                backupManager.read(fileName = fileName)
            }.onSuccess {
                val (importedData, missingData) = importDataChecker.checkRequiredData(it)
                _viewState.update {
                    it.copy(
                        importedData = importedData,
                        missingData = missingData,
                        isLoading = false,
                        initialErrorCount = missingData.size
                    )
                }
            }.onFailure {
                _snackbarState.update {
                    it
                }
            }

        }
    }

    fun onFixError(missingData: ImportedData.MissingData) {
        awaitingMissingData = missingData
        when (missingData.dataType) {
            ImportedData.MissingData.DataType.ACCOUNT_FROM, ImportedData.MissingData.DataType.ACCOUNT_TO -> {
                routeNavigator.navigateTo(AccountRoutes.editorDestination(missingData.name))
            }

            ImportedData.MissingData.DataType.INCOME_CATEGORY -> {
                routeNavigator.navigateTo(
                    CategoryRoutes.ImportCategory(
                        transactionType = TransactionType.INCOME,
                        newCategoryName = missingData.name
                    )
                )
            }

            ImportedData.MissingData.DataType.EXPENSES_CATEGORY -> {
                routeNavigator.navigateTo(
                    CategoryRoutes.ImportCategory(
                        transactionType = TransactionType.EXPENSES,
                        newCategoryName = missingData.name
                    )
                )
            }

            ImportedData.MissingData.DataType.TAG -> {
                routeNavigator.navigateTo(TagRoutes.addNewTagDestination(true, missingData.name))
            }
        }
    }

    private fun toggleLoader(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    fun saveData() {
        viewModelScope.launch {
            toggleLoader(true)
            val primaryCurrency = userCurrencyRepository.getPrimaryCurrency()
            val validImportedData = _viewState.value.importedData.filter { it.isValid }
            val importedTransaction = validImportedData.map {
                val fromAccount = (it.accountFrom as? ImportedData.RequiredDataState.Acquired)?.id
                val toAccount = (it.accountTo as? ImportedData.RequiredDataState.Acquired)?.id
                val category = (it.categoryColumns as? ImportedData.RequiredDataState.Acquired)?.id
                val tags = it.tags.mapNotNull {
                    (it as? ImportedData.RequiredDataState.Acquired)?.id
                }
                val accountCurrency = when (TransactionType.fromString(it.transactionType)) {
                    TransactionType.EXPENSES -> {
                        fromAccount?.let { accountRepository.getAccountById(it) }?.currency
                    }
                    TransactionType.INCOME, TransactionType.TRANSFER -> {
                        toAccount?.let { accountRepository.getAccountById(it) }?.currency
                    }
                    else -> it.currency
                } ?: kotlin.run {
                    toggleLoader(false)
                    _snackbarState.value = "Something went wrong. Could not obtain account currency when it should able to."
                    return@launch
                }

                TransactionDomainInput(
                    transactionName = it.transactionName,
                    transactionAccountFrom = fromAccount,
                    transactionAccountTo = toAccount,
                    transactionTypeCode = it.transactionType,
                    minorUnitAmount = currencyFormatter.from(it.amount, it.currency),
                    currency = it.currency,
                    accountMinorUnitAmount = currencyFormatter.from(it.accountAmount, accountCurrency),
                    primaryMinorUnitAmount = currencyFormatter.from(it.primaryAmount, primaryCurrency),
                    transactionDate = it.date,
                    transactionCategoryId = category,
                    tagIds = tags,
                    schedulerId = null
                )
            }
            resultOf {
                transactionTagRepository.insertAllTransactions(importedTransaction)
            }.fold(
                onSuccess = {
                    toggleLoader(false)
                    routeNavigator.pop()
                },
                onFailure = { e ->
                    toggleLoader(false)
                    _snackbarState.value = e.toString()
                }
            )
        }
    }
}

internal data class ImportDataViewState(
    val isFeatureEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val importedData: List<ImportedData> = listOf(),
    val missingData: Set<ImportedData.MissingData> = setOf(),
    val progress: Float = 0f,
    private val initialErrorCount: Int = 0,
) {
    val importFixPercentage: Float
        get() {
            return if (initialErrorCount == 0) return 1f
            else 1 - missingData.size/initialErrorCount.toFloat()
        }
}

internal data class ImportedData(
    val transactionName: String,
    val accountFrom: RequiredDataState?,
    val accountTo: RequiredDataState?,
    val transactionType: String,
    val currency: String,
    val accountAmount: Double,
    val primaryAmount: Double,
    val amount: Double,
    val date: String,
    val categoryColumns: RequiredDataState?,
    val tags: List<RequiredDataState>
) {
    val isValid: Boolean
        get() = accountFrom !is RequiredDataState.Missing || accountTo !is RequiredDataState.Missing || categoryColumns !is RequiredDataState.Missing || tags.filterIsInstance<RequiredDataState.Missing>().isEmpty()

    sealed interface RequiredDataState {
        val name: String

        data class Acquired(
            val id: Int,
            override val name: String,
        ) : RequiredDataState

        data class Missing(
            override val name: String,
        ) : RequiredDataState
    }

    data class MissingData(
        val name: String,
        val dataType: DataType,
        val transactionIndex: Set<Int>
    ) {
        enum class DataType {
            INCOME_CATEGORY, EXPENSES_CATEGORY, ACCOUNT_FROM, ACCOUNT_TO, TAG
        }
    }
}