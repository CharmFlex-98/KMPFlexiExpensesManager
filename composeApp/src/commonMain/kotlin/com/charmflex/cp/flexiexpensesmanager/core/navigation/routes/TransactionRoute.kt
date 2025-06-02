package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

object TransactionRoute {
    const val ROOT = "TRANSACTION"
    const val NEW_TRANSACTION = "$ROOT/NEW_TRANSACTION"
    const val NEW_SCHEDULE_TRANSACTION = "$ROOT/NEW_SCHEDULE_TRANSFER"
    const val TRANSACTION_EDITOR = "$ROOT/TRANSACTION_EDITOR"
    const val SCHEDULED_TRANSACTION_EDITOR = "$ROOT/SCHEDULED_TRANSACTION_EDITOR"
    const val TRANSACTION_DETAIL = "$ROOT/TRANSACTION_DETAIL"

    object Args {
        const val REFRESH_HOME = "$ROOT/Refresh"
        const val TRANSACTION_ID = "TRANSACTION_ID"
        const val SCHEDULE_TRANSACTION_ID = "SCHEDULE_TRANSFER_ID"
    }

    @Serializable
    data class Detail(
        val id: Long
    ) : NavigationRoute

    @Serializable
    object NewTransaction : NavigationRoute

    @Serializable
    object ScheduledTransaction : NavigationRoute

    @Serializable
    data class TransactionEditor(
        val transactionId: Long
    ) : NavigationRoute

    @Serializable
    data class ScheduledTransactionEditor(
        val scheduledTransactionId: Int
    ) : NavigationRoute


    val transactionDetail = buildRoute(TRANSACTION_DETAIL) {
        addArg(Args.TRANSACTION_ID)
    }

    val newTransaction = buildRoute(NEW_TRANSACTION) {}

    val newScheduledTransaction = buildRoute(NEW_SCHEDULE_TRANSACTION) {}

    val transactionEditor = buildRoute(TRANSACTION_EDITOR) {
        addArg(Args.TRANSACTION_ID)
    }
    val scheduledTransactionEditor = buildRoute(SCHEDULED_TRANSACTION_EDITOR) {
        addArg(Args.SCHEDULE_TRANSACTION_ID)
    }

    internal fun newTransactionDestination(): NavigationRoute = NewTransaction

    internal fun newScheduleTransactionDestination(): NavigationRoute = ScheduledTransaction

    internal fun editTransactionDestination(transactionId: Long): NavigationRoute {
        return TransactionEditor(transactionId)
    }

    internal fun transactionDetailDestination(transactionId: Long): NavigationRoute {
        return Detail(transactionId)
    }
}