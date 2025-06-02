package com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction

import AccountRepository
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.utils.CurrencyFormatter
import com.charmflex.flexiexpensesmanager.core.utils.CurrencyVisualTransformation
import com.charmflex.cp.flexiexpensesmanager.core.utils.RateExchangeManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.minusYears
import com.charmflex.cp.flexiexpensesmanager.core.utils.di.getDep
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.di.modules.TransactionEditorProvider
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.UserCurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.currency.service.CurrencyService
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.repositories.TagRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.repositories.TransactionRepository
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TransactionEditorContentProvider
import com.charmflex.cp.flexiexpensesmanager.features.transactions.usecases.SubmitTransactionUseCase
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_update_account
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import org.koin.core.qualifier.named

@Factory
internal class TransactionEditorViewModelFactory constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val routeNavigator: RouteNavigator,
    private val transactionCategoryRepository: TransactionCategoryRepository,
    private val submitTransactionUseCase: SubmitTransactionUseCase,
    private val currencyVisualTransformationBuilder: CurrencyVisualTransformation.Builder,
    private val tagRepository: TagRepository,
    private val resourcesProvider: ResourcesProvider,
    private val currencyService: CurrencyService,
    private val currencyFormatter: CurrencyFormatter,
    private val userCurrencyRepository: UserCurrencyRepository,
    private val rateExchangeManager: RateExchangeManager
) {
    fun create(transactionId: Long?): TransactionEditorViewModel {
        val contentProvider: TransactionEditorContentProvider = getDep(named(TransactionEditorProvider.DEFAULT))
        return TransactionEditorViewModel(
            transactionId,
            transactionRepository,
            submitTransactionUseCase,
            contentProvider,
            accountRepository,
            routeNavigator,
            transactionCategoryRepository,
            currencyVisualTransformationBuilder,
            tagRepository,
            currencyService,
            currencyFormatter,
            userCurrencyRepository,
            rateExchangeManager,
            resourcesProvider,
        )
    }
}


internal class TransactionEditorViewModel  constructor(
    private val transactionId: Long?,
    private val transactionRepository: TransactionRepository,
    private val submitTransactionUseCase: SubmitTransactionUseCase,
    contentProvider: TransactionEditorContentProvider,
    accountRepository: AccountRepository,
    routeNavigator: RouteNavigator,
    transactionCategoryRepository: TransactionCategoryRepository,
    currencyVisualTransformationBuilder: CurrencyVisualTransformation.Builder,
    tagRepository: TagRepository,
    currencyService: CurrencyService,
    currencyFormatter: CurrencyFormatter,
    userCurrencyRepository: UserCurrencyRepository,
    rateExchangeManager: RateExchangeManager,
    private val resourcesProvider: ResourcesProvider
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
    transactionId,
) {
    init {
        initialise()
    }

    override fun calendarSelectionRange(): ClosedRange<LocalDate> {
        return localDateNow().minusYears(10)..localDateNow()
    }

    override suspend fun loadTransaction(id: Long): TransactionEditorDataUI? {
        val res = transactionRepository.getTransactionById(id).firstOrNull() ?: return null
        return TransactionEditorDataUI(
            res.transactionName,
            res.transactionAccountFrom,
            res.transactionAccountTo,
            res.transactionTypeCode,
            res.minorUnitAmount,
            res.accountMinorUnitAmount,
            res.primaryMinorUnitAmount,
            res.currency,
            res.transactionDate,
            res.transactionCategory,
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
        return submitTransactionUseCase.submitExpenses(
            id,
            name,
            fromAccountId,
            amount,
            categoryId,
            transactionDate,
            currency,
            accountMinorUnitAmount,
            primaryMinorUnitAmount,
            tagIds
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
        return submitTransactionUseCase.submitIncome(
            id,
            name,
            toAccountId,
            amount,
            categoryId,
            transactionDate,
            currency,
            primaryMinorUnitAmount
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
        return submitTransactionUseCase.submitTransfer(
            id,
            name,
            fromAccountId,
            toAccountId,
            amount,
            transactionDate,
            currency,
            accountMinorUnitAmount
        )
    }

    override suspend fun submitUpdate(
        id: Long?,
        accountId: Int,
        isIncrement: Boolean,
        amount: Long,
        transactionDate: String,
    ): Result<Unit> {
        val hint =
            if (isIncrement) UpdateAccountType.INCREMENT.name else UpdateAccountType.DEDUCTION.name
        val name = resourcesProvider.getString(Res.string.generic_update_account)
        return submitTransactionUseCase.submitUpdateAccount(
            id, "$name ($hint)", accountId, isIncrement, amount, transactionDate
        )
    }

    override fun getType(): TransactionRecordableType {
        return transactionId?.let {
            TransactionRecordableType.EDIT_TRANSACTION
        } ?: TransactionRecordableType.NEW_TRANSACTION
    }
}