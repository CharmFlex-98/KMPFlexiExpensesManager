package com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting

import BillingRoutes
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.features.backup.TransactionBackupManager
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AccountRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AnnouncementRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BackupRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BudgetRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.flexiexpensesmanager.core.navigation.routes.CurrencyRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.SchedulerRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TagRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.getLocalDateTime
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataClearServiceType
import com.charmflex.cp.flexiexpensesmanager.features.backup.AppDataService
import com.charmflex.cp.flexiexpensesmanager.features.billing.exceptions.NetworkError
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.FeatureFlagService
import com.charmflex.cp.flexiexpensesmanager.features.remote.feature_flag.model.PremiumFeature
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
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
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.Factory

@Factory
internal class SettingViewModel constructor(
    private val routeNavigator: RouteNavigator,
    private val transactionBackupManager: TransactionBackupManager,
    private val appDataService: AppDataService,
    private val resourcesProvider: ResourcesProvider,
    private val featureFlagService: FeatureFlagService,
    private val toastManager: ToastManager
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
                                val fileName = "FEM_export_${getLocalDateTime()}.xlsx"
                                resultOf {
                                    transactionBackupManager.write(fileName)
                                }.fold(
                                    onSuccess = {
                                        toggleLoader(false)
                                        toastManager.postSuccess(resourcesProvider.getString(Res.string.export_completed))
                                        onCreatedCompleted(fileName)
                                    },
                                    onFailure = {
                                        toggleLoader(false)
                                        toastManager.postError("${resourcesProvider.getString(Res.string.export_error_toast)}${it.message}")
                                    }
                                )

                                return@launch
                            }

                            toastManager.postError(resourcesProvider.getString(Res.string.premium_feature_toast_hint))
                            toggleLoader(false)
                        }
                        .onFailure { err ->
                            if (err is NetworkError) {
                                toastManager.postError(resourcesProvider.getString(Res.string.network_error))
                            } else {
                                toastManager.postError(err.message)
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

            SettingAction.REFERRAL -> {
                // Under development
                routeNavigator.navigateTo(AnnouncementRoute.Root(RemoteConfigScene.REFERRAL))
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
                    toastManager.postError(resourcesProvider.getString(Res.string.clear_all_data_failure_toast))
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
                title = resourcesProvider.getString(Res.string.setting_expenses_category),
                action = SettingAction.EXPENSES_CAT
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_income_category),
                action = SettingAction.INCOME_CAT
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_account),
                action = SettingAction.ACCOUNT
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_primary_currency),
                action = SettingAction.PRIMARY_CURRENCY
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_export),
                action = SettingAction.Export
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_import),
                action = SettingAction.Import
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_tag),
                action = SettingAction.Tag
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
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_referral),
                action = SettingAction.REFERRAL
            ),
            SettingActionable(
                title = resourcesProvider.getString(Res.string.setting_reset_data),
                action = SettingAction.RESET_DATA
            ),
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
        enum class ResetType {
            TRANSACTION_ONLY, ALL
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
    EXPENSES_CAT, INCOME_CAT, ACCOUNT, PRIMARY_CURRENCY, SECONDARY_CURRENCY, Tag, Export, Import, RESET_DATA, SCHEDULER, BUDGET, BILLING, REFERRAL
}