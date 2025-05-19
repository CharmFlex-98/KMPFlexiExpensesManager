package com.charmflex.flexiexpensesmanager.features.scheduler

//
//internal class TransactionSchedulerWorkerFactory @Inject constructor(
//    private val getScheduleTransactionNextDateUseCase: GetScheduleTransactionNextDateUseCase,
//    private val scheduledTransactionSchedulerRepository: TransactionSchedulerRepository,
//    private val submitTransactionUseCase: SubmitTransactionUseCase
//) : WorkerFactory() {
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): ListenableWorker {
//        return TransactionSchedulerWorker(
//            appContext,
//            workerParameters,
//            getScheduleTransactionNextDateUseCase,
//            scheduledTransactionSchedulerRepository,
//            submitTransactionUseCase
//        )
//    }
//
//}

//internal class TransactionSchedulerWorker @Inject constructor(
//    appContext: Context,
//    workerParams: WorkerParameters,
//    private val getScheduleTransactionNextDateUseCase: GetScheduleTransactionNextDateUseCase,
//    private val scheduledTransactionSchedulerRepository: TransactionSchedulerRepository,
//    private val submitTransactionUseCase: SubmitTransactionUseCase
//) : CoroutineWorker(appContext, workerParams) {
//
//    override suspend fun doWork(): Result {
//        Log.d("worker", "do work")
//        val dataString = inputData.keyValueMap[InputDataKey] as String
//        val data = Json.decodeFromString<ScheduledTransaction>(dataString)
//        val last =
//            scheduledTransactionSchedulerRepository.getScheduledTransactionLastUpdateDate(data.id)
//
//        // Ignore if no need to add transaction
//        if (shouldAddTransaction(data, last).not()) return Result.success()
//
//        Log.d("worker", "will add transaction")
//
//        return when (data.transactionType) {
//            TransactionType.EXPENSES -> {
//                submitTransactionUseCase.submitExpenses(
//                    id = null,
//                    name = data.transactionName,
//                    transactionDate = getScheduleTransactionNextDateUseCase(data),
//                    categoryId = data.category!!.id,
//                    fromAccountId = data.accountFrom!!.accountId,
//                    amount = data.amountInCent,
//                    currency = data.currency,
//                    rate = data.rate,
//                    tagIds = listOf()
//                ).fold(
//                    onSuccess = {
//                        scheduledTransactionSchedulerRepository.updateScheduledTransactionLastUpdate(
//                            data.id,
//                            LocalDate.now().toStringWithPattern(DATE_ONLY_DEFAULT_PATTERN)
//                        )
//                        Result.success()
//                    },
//                    onFailure = {
//                        Result.failure()
//                    }
//                )
//            }
//
//            else -> {
//                Result.failure()
//            }
//        }
//    }
//
//    private fun shouldAddTransaction(
//        scheduledTransaction: ScheduledTransaction,
//        lastUpdate: LocalDate?
//    ): Boolean {
//        if (lastUpdate == null) return false
//
//        return when (scheduledTransaction.schedulerPeriod) {
//            SchedulerPeriod.MONTHLY -> {
//                val nextMonth = lastUpdate.atStartOfDay().plusMonths(1)
//                return nextMonth.isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)))
//            }
//
//            else -> false
//        }
//    }
//}