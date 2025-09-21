package com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting

import BillingRoutes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.features.backup.TransactionBackupManager
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AccountRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BackupRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BudgetRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.flexiexpensesmanager.core.navigation.routes.CurrencyRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.SchedulerRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TagRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.getLocalDateTime
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataClearServiceType
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataService
import com.charmflex.cp.flexiexpensesmanager.features.billing.exceptions.NetworkError
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.FeatureFlagService
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.koin.core.annotation.Factory

@Factory
internal class SettingViewModel constructor(
    private val routeNavigator: RouteNavigator,
    private val transactionBackupManager: TransactionBackupManager,
    private val appDataService: AppDataService,
    private val resourcesProvider: ResourcesProvider,
    private val featureFlagService: FeatureFlagService
) : ViewModel() {

    private val _onDataClearedEvent: MutableSharedFlow<OnDataCleared> =
        MutableSharedFlow(extraBufferCapacity = 1)
    val onDataClearedEvent = _onDataClearedEvent.asSharedFlow()

    private val _viewState = MutableStateFlow(SettingViewState())
    val viewState = _viewState.asStateFlow()

    var snackBarState = mutableStateOf<SnackBarState>(SnackBarState.None)
        private set

    init {
        _viewState.update {
            it.copy(
                isLoading = false,
                items = getSettingActionable()
            )
        }
    }

    fun onCreatedCompleted(fileName: String) {
        viewModelScope.launch {
            transactionBackupManager.share(fileName)
        }
    }

    fun onTap(settingAction: SettingAction) {
        when (settingAction) {
            SettingAction.EXPENSES_CAT -> {
                routeNavigator.navigateTo(CategoryRoutes.editorDestination(TransactionType.EXPENSES))
            }

            SettingAction.INCOME_CAT -> {
                routeNavigator.navigateTo(CategoryRoutes.editorDestination(TransactionType.INCOME))
            }

            SettingAction.ACCOUNT -> {
                routeNavigator.navigateTo(AccountRoutes.editorDestination())
            }

            SettingAction.PRIMARY_CURRENCY -> {
                routeNavigator.navigateTo(CurrencyRoutes.currencySettingDestination(CurrencyRoutes.Args.CURRENCY_TYPE_MAIN))
            }

            SettingAction.SECONDARY_CURRENCY -> {
                routeNavigator.navigateTo(CurrencyRoutes.UserSecondaryCurrency)
            }

            SettingAction.Tag -> {
                routeNavigator.navigateTo(TagRoutes.addNewTagDestination())
            }

            SettingAction.Export -> {
                viewModelScope.launch {
                    toggleLoader(true)
                    featureFlagService.isPremiumFeatureAllowed(PremiumFeature.BACKUP)
                        .onSuccess { enabled ->
                            if (enabled) {
                                resultOf {
                                    transactionBackupManager.write("test_export.xlsx")
                                }.fold(
                                    onSuccess = {
                                        toggleLoader(false)
                                        snackBarState.value = SnackBarState.Success("Done exporting")
                                        onCreatedCompleted("fem_export_${getLocalDateTime()}.xlsx")
                                    },
                                    onFailure = {
                                        toggleLoader(false)
                                        snackBarState.value =
                                            SnackBarState.Error("Error exporting: ${it.message}")
                                    }
                                )

                                return@launch
                            }

                            snackBarState.value =
                                SnackBarState.Error("This is a premium feature. Unlock it to access the feature.")
                            toggleLoader(false)
                        }
                        .onFailure { err ->
                            if (err is NetworkError) {
                                snackBarState.value = SnackBarState.Error(resourcesProvider.getString(Res.string.network_error))
                            } else {
                                snackBarState.value = SnackBarState.Error(err.message)
                            }

                            toggleLoader(false)
                        }
                }
            }

            SettingAction.Import -> {
                routeNavigator.navigateTo(BackupRoutes.ImportSetting)
            }

            SettingAction.RESET_DATA -> {
                _viewState.update {
                    it.copy(
                        dialogState = SettingDialogState.ResetDataDialogState(
                            selection = null
                        )
                    )
                }
            }

            SettingAction.SCHEDULER -> {
                routeNavigator.navigateTo(SchedulerRoutes.SchedulerList)
            }

            SettingAction.BUDGET -> {
                routeNavigator.navigateTo(BudgetRoutes.BudgetSetting)
            }

            SettingAction.BILLING -> {
                routeNavigator.navigateTo(BillingRoutes.Root)
            }
        }
    }

    fun toggleResetSelection(selection: SettingDialogState.ResetDataDialogState.ResetType) {
        val resetDataDialogState =
            _viewState.value.dialogState as? SettingDialogState.ResetDataDialogState
        resetDataDialogState?.let { dialogState ->
            _viewState.update {
                it.copy(
                    dialogState = dialogState.copy(
                        selection = selection
                    )
                )
            }
        }
    }

    fun refreshSnackBarState() {
        snackBarState.value = SnackBarState.None
    }

    fun closeDialog() {
        _viewState.update {
            it.copy(
                dialogState = null
            )
        }
    }

    fun onFactoryResetConfirmed() {
        val selection =
            (_viewState.value.dialogState as? SettingDialogState.ResetDataDialogState)?.selection
        closeDialog()

        selection?.let {
            when (it) {
                SettingDialogState.ResetDataDialogState.ResetType.TRANSACTION_ONLY -> clearAllTransactions()
                else -> clearAllData()
            }
        }
    }

    private fun clearAllTransactions() {
        viewModelScope.launch {
            toggleLoader(true)
            appDataService.clearAppData(AppDataClearServiceType.TRANSACTION_ONLY)
            toggleLoader(false)
            _onDataClearedEvent.tryEmit(OnDataCleared.Refresh)
        }
    }

    private fun clearAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            toggleLoader(true)
            resultOf {
                appDataService.clearAppData(AppDataClearServiceType.ALL)
            }.fold(
                onSuccess = {
                    _onDataClearedEvent.tryEmit(OnDataCleared.Finish)
                },
                onFailure = {
                    snackBarState.value = SnackBarState.Error("Failed to clear all data.")
                }
            )
            toggleLoader(false)
        }
    }

    private fun toggleLoader(isLoading: Boolean) {
        _viewState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun getSettingActionable(): List<SettingActionable> {
        return listOf(
            SettingActionable(
                title = "Set expenses category",
                action = SettingAction.EXPENSES_CAT
            ),
            SettingActionable(
                title = "Set income category",
                action = SettingAction.INCOME_CAT
            ),
            SettingActionable(
                title = "Set account",
                action = SettingAction.ACCOUNT
            ),
            SettingActionable(
                title = "Set primary currency",
                action = SettingAction.PRIMARY_CURRENCY
            ),
            SettingActionable(
                title = "Export",
                action = SettingAction.Export
            ),
            SettingActionable(
                title = "Import",
                action = SettingAction.Import
            ),
            SettingActionable(
                title = "Reset Data",
                action = SettingAction.RESET_DATA
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_scheduler_title),
                action = SettingAction.SCHEDULER
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_budget_title),
                action = SettingAction.BUDGET
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_billing_title),
                action = SettingAction.BILLING
            )
        )
    }
}

internal data class SettingViewState(
    val isLoading: Boolean = false,
    val dialogState: SettingDialogState? = null,
    val items: List<SettingActionable> = listOf(),
)

internal sealed interface SettingDialogState {
    val title: StringResource

    val subtitle: StringResource

    data class ResetDataDialogState(
        override val title: StringResource = Res.string.setting_factory_reset_title,
        override val subtitle: StringResource = Res.string.setting_factory_reset_dialog_subtitle,
        val selection: ResetType?
    ) : SettingDialogState {
        enum class ResetType(val value: String) {
            TRANSACTION_ONLY("Transaction Only"), ALL("All")
        }
    }
}

internal sealed interface OnDataCleared {
    object Refresh : OnDataCleared
    object Finish : OnDataCleared
}

internal data class SettingActionable(
    val title: String,
    val action: SettingAction
)

internal enum class SettingAction {
    EXPENSES_CAT, INCOME_CAT, ACCOUNT, PRIMARY_CURRENCY, SECONDARY_CURRENCY, Tag, Export, Import, RESET_DATA, SCHEDULER, BUDGET, BILLING
}