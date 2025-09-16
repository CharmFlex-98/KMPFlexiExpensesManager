package com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor

import AccountRepository
import com.charmflex.cp.flexiexpensesmanager.core.domain.FEField
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.minusYears
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.plusMonths
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyVisualTransformationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.utils.RateExchangeManager
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.SchedulerPeriod
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.repository.TransactionSchedulerRepository
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.usecases.SubmitTransactionSchedulerUseCase
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.repositories.TagRepository
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.currency.service.CurrencyService
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TRANSACTION_SCHEDULER_PERIOD
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorBaseViewModel
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionEditorDataUI
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.TransactionRecordableType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDate

internal class SchedulerEditorViewModelFactory constructor(
    private val accountRepository: AccountRepository,
    private val transactionSchedulerRepository: TransactionSchedulerRepository,
    private val submitTransactionSchedulerUseCase: SubmitTransactionSchedulerUseCase,
    private val routeNavigator: RouteNavigator,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val currencyVisualTransformationBuilder: CurrencyVisualTransformationBuilder,
    private val currencyService: CurrencyService,
    private val currencyFormatter: CurrencyFormatter,
    private val rateExchangeManager: RateExchangeManager,
    private val userCurrencyRepository: UserCurrencyRepository,
    private val tagRepository: TagRepository,
    private val contentProvider: TransactionEditorContentProvider
) {
    fun create(schedulerId: Long?): SchedulerEditorViewModel {
        return SchedulerEditorViewModel(
            schedulerId,
            transactionSchedulerRepository,
            submitTransactionSchedulerUseCase,
            contentProvider,
            accountRepository,
            routeNavigator,
            transactionCategoryRepository,
            currencyVisualTransformationBuilder,
            currencyService,
            currencyFormatter,
            rateExchangeManager,
            userCurrencyRepository,
            tagRepository,
        )
    }
}


internal class SchedulerEditorViewModel(
    private val schedulerId: Long?,
    private val transactionSchedulerRepository: TransactionSchedulerRepository,
    private val submitTransactionSchedulerUseCase: SubmitTransactionSchedulerUseCase,
    contentProvider: TransactionEditorContentProvider,
    accountRepository: AccountRepository,
    routeNavigator: RouteNavigator,
    transactionCategoryRepository: TransactionCategoryRepository,
    currencyVisualTransformationBuilder: CurrencyVisualTransformationBuilder,
    currencyService: CurrencyService,
    currencyFormatter: CurrencyFormatter,
    rateExchangeManager: RateExchangeManager,
    userCurrencyRepository: UserCurrencyRepository,
    tagRepository: TagRepository,
) : TransactionEditorBaseViewModel(
    contentProvider,
    accountRepository,
    routeNavigator,
    transactionCategoryRepository,
    currencyVisualTransformationBuilder,
    tagRepository,
    currencyService,
    currencyFormatter,
    rateExchangeManager,
    userCurrencyRepository,
    schedulerId
) {

    override val transactionType: List<TransactionType>
        get() = super.transactionType.filter { it != TransactionType.UPDATE_ACCOUNT }

    init {
        initialise()
    }

    private val selectedPeriod = MutableStateFlow(SchedulerPeriod.MONTHLY)

    override suspend fun loadTransaction(id: Long): TransactionEditorDataUI? {
        val res = transactionSchedulerRepository.getTransactionSchedulerById(id) ?: return null
        return TransactionEditorDataUI(
            res.transactionName,
            res.accountFrom,
            res.accountTo,
            res.transactionType.name,
            res.minorUnitAmount,
            res.accountMinorUnitAmount,
            res.primaryMinorUnitAmount,
            res.currency,
            res.startUpdateDate,
            res.category,
            res.tags
        )
    }

    override suspend fun submitExpenses(
        id: Long?,
        name: String,
        fromAccountId: Int,
        amount: Long,
        categoryId: Int,
        transactionDate: String,
        currency: String,
        accountMinorUnitAmount: Long,
        primaryMinorUnitAmount: Long,
        tagIds: List<Int>,
    ): Result<Unit> {
        return submitTransactionSchedulerUseCase.submitExpenses(
            id,
            name,
            fromAccountId,
            amount,
            categoryId,
            transactionDate,
            transactionDate,
            currency,
            accountMinorUnitAmount,
            primaryMinorUnitAmount,
            tagIds,
            selectedPeriod.value
        )
    }

    override suspend fun submitIncome(
        id: Long?,
        name: String,
        toAccountId: Int,
        amount: Long,
        categoryId: Int,
        transactionDate: String,
        currency: String,
        primaryMinorUnitAmount: Long,
        tagIds: List<Int>,
    ): Result<Unit> {
        return submitTransactionSchedulerUseCase.submitIncome(
            id,
            name,
            toAccountId,
            amount,
            categoryId,
            transactionDate,
            transactionDate,
            currency,
            primaryMinorUnitAmount,
            tagIds,
            selectedPeriod.value,
        )
    }

    override suspend fun submitTransfer(
        id: Long?,
        name: String,
        fromAccountId: Int,
        toAccountId: Int,
        amount: Long,
        transactionDate: String,
        currency: String,
        accountMinorUnitAmount: Long,
        tagIds: List<Int>,
    ): Result<Unit> {
        return submitTransactionSchedulerUseCase.submitTransfer(
            id,
            name,
            fromAccountId,
            toAccountId,
            amount,
            transactionDate,
            transactionDate,
            currency,
            accountMinorUnitAmount,
            selectedPeriod.value,
            tagIds
        )
    }

    override suspend fun submitUpdate(
        id: Long?,
        accountId: Int,
        isIncrement: Boolean,
        amount: Long,
        transactionDate: String,
    ): Result<Unit> {
        // Do nothing
        return Result.failure(Throwable("Should not be able to call here"))
    }

    override fun onPeriodSelected(period: SchedulerPeriod, targetField: FEField?) {
        super.onPeriodSelected(period, targetField)
        selectedPeriod.value = period
    }

    override fun calendarSelectionRange(): ClosedRange<LocalDate> {
        return localDateNow().minusYears(10)..localDateNow().plusMonths(3)
    }

    override fun allowProceed(): Boolean {
        return super.allowProceed() &&
                (viewState.value.fields.firstOrNull { it.id == TRANSACTION_SCHEDULER_PERIOD }?.id?.isNotBlank() ?: false)
    }

    override fun getType(): TransactionRecordableType {
        return schedulerId?.let {
            TransactionRecordableType.EDIT_SCHEDULER
        } ?: TransactionRecordableType.NEW_SCHEDULER
    }
}