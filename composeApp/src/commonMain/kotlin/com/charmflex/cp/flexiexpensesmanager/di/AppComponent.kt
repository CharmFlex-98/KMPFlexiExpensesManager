package com.charmflex.cp.flexiexpensesmanager.di

import com.charmflex.cp.flexiexpensesmanager.features.account.di.AccountInjector
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.AccountEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.backup.di.BackupInjector
import com.charmflex.cp.flexiexpensesmanager.features.backup.ui.ImportDataViewModel
import com.charmflex.cp.flexiexpensesmanager.features.budget.di.BudgetInjector
import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.setting.BudgetSettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.budget.ui.stats.BudgetDetailViewModel
import com.charmflex.cp.flexiexpensesmanager.features.category.category.di.CategoryInjector
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.CategoryEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.stat.CategoryStatViewModel
import com.charmflex.cp.flexiexpensesmanager.features.currency.di.CurrencyInjector
import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.CurrencySettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.currency.ui.UserCurrencyViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.di.HomeInjector
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.HomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.account.AccountHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard.DashboardViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.history.TransactionHomeViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting.SettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_heat_map.ExpensesHeatMapViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart.ExpensesChartViewModel
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.SchedulerInjector
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList.SchedulerListViewModel
import com.charmflex.cp.flexiexpensesmanager.features.session.SessionManager
import com.charmflex.cp.flexiexpensesmanager.features.session.di.SessionInjector
import com.charmflex.cp.flexiexpensesmanager.features.transactions.di.TransactionInjector
import com.charmflex.cp.flexiexpensesmanager.core.di.MainInjector
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.AuthInjector
import com.charmflex.cp.flexiexpensesmanager.features.tag.di.TagInjector
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.account_detail.AccountDetailViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.auth.ui.landing.LandingScreenViewModel
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.di.BillingInjector
import com.charmflex.cp.flexiexpensesmanager.features.billing.ui.BillingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.detail.CategoryDetailViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_detail.SchedulerDetailViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.SchedulerEditorViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.tag.ui.TagSettingViewModel
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_detail.TransactionDetailViewModelFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Module
@ComponentScan("com.charmflex.cp.flexiexpensesmanager")
class AppModule

internal class AppComponent : MainInjector, AuthInjector, BudgetInjector, HomeInjector,
    TransactionInjector, SchedulerInjector,
    AccountInjector, BackupInjector, CategoryInjector, CurrencyInjector, SessionInjector,
    TagInjector, BillingInjector,
    KoinComponent {

    override fun landingScreenViewModel(): LandingScreenViewModel = get()
    override fun budgetSettingViewModel(): BudgetSettingViewModel = get()
    override fun budgetDetailViewModel(): BudgetDetailViewModel = get()
    override fun homeViewModel(): HomeViewModel = get()
    override fun dashBoardViewModel(): DashboardViewModel = get()
    override fun expensesPieChartViewModel(): ExpensesChartViewModel = get()
    override fun expensesHeatMapViewModel(): ExpensesHeatMapViewModel = get()
    override fun expensesHistoryViewModel(): TransactionHomeViewModel = get()
    override fun accountHomeViewModel(): AccountHomeViewModel = get()
    override fun settingViewModel(): SettingViewModel = get()
    override fun transactionEditorViewModelFactory(): TransactionEditorViewModelFactory = get()
    override fun transactionDetailViewModelFactory(): TransactionDetailViewModelFactory = get()
    override fun schedulerListViewModel(): SchedulerListViewModel = get()
    override fun schedulerEditorViewModelFactory(): SchedulerEditorViewModelFactory = get()
    override fun schedulerDetailViewModelFactory(): SchedulerDetailViewModelFactory = get()
    override fun accountEditorViewModel(): AccountEditorViewModel = get()
    override fun accountDetailViewModelFactory(): AccountDetailViewModelFactory = get()
    override fun importDataViewModel(): ImportDataViewModel = get()
    override fun categoryEditorViewModel(): CategoryEditorViewModel = get()
    override fun categoryStatViewModel(): CategoryStatViewModel = get()
    override fun categoryDetailViewModelFactory(): CategoryDetailViewModelFactory = get()
    override fun currencySettingViewModel(): CurrencySettingViewModel = get()
    override fun userCurrencyViewModel(): UserCurrencyViewModel = get()
    override fun sessionManager(): SessionManager = get()
    override fun routeNavigator(): RouteNavigator = get()
    override fun toastManager(): ToastManager = get()
    override fun tagSettingViewModel(): TagSettingViewModel = get()
    override fun billingViewModel(): BillingViewModel = get()
}