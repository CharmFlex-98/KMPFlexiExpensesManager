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
import com.charmflex.cp.flexiexpensesmanager.features.auth.di.AuthInjector
import com.charmflex.cp.flexiexpensesmanager.features.tag.di.TagInjector
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.account_detail.AccountDetailViewModelFactory
import com.charmflex.cp.flexiexpensesmanager.features.auth.ui.landing.LandingScreenViewModel
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.detail.CategoryDetailViewModelFactory
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
    TagInjector,
    KoinComponent {
    override val landingScreenViewModel: LandingScreenViewModel by inject()
    override val budgetSettingViewModel: BudgetSettingViewModel by inject()
    override val budgetDetailViewModel: BudgetDetailViewModel by inject()
    override val homeViewModel: HomeViewModel by inject()
    override val dashBoardViewModel: DashboardViewModel by inject()
    override val expensesPieChartViewModel: ExpensesChartViewModel by inject()
    override val expensesHeatMapViewModel: ExpensesHeatMapViewModel by inject()
    override val expensesHistoryViewModel: TransactionHomeViewModel by inject()
    override val accountHomeViewModel: AccountHomeViewModel by inject()
    override val settingViewModel: SettingViewModel by inject()
    override val transactionEditorViewModelFactory: TransactionEditorViewModelFactory by inject()
    override val transactionDetailViewModelFactory: TransactionDetailViewModelFactory by inject()
    override val schedulerListViewModel: SchedulerListViewModel by inject()
    override val schedulerEditorViewModelFactory: SchedulerEditorViewModelFactory by inject()
    override val accountEditorViewModel: AccountEditorViewModel by inject()
    override val accountDetailViewModelFactory: AccountDetailViewModelFactory by inject()
    override val importDataViewModel: ImportDataViewModel by inject()
    override val categoryEditorViewModel: CategoryEditorViewModel by inject()
    override val categoryStatViewModel: CategoryStatViewModel by inject()
    override val categoryDetailViewModelFactory: CategoryDetailViewModelFactory by inject()
    override val currencySettingViewModel: CurrencySettingViewModel by inject()
    override val userCurrencyViewModel: UserCurrencyViewModel by inject()
    override val sessionManager: SessionManager by inject()
    override val routeNavigator: RouteNavigator by inject()
    override val tagSettingViewModel: TagSettingViewModel by inject()

    inline fun <reified T: Any> getDep(
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition? = null,
    ): T {
        return get<T>()
    }
}