package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

object TransactionRoute {
    object Args {
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


    internal fun newTransactionDestination(): NavigationRoute = NewTransaction

    internal fun newScheduleTransactionDestination(): NavigationRoute = ScheduledTransaction

    internal fun editTransactionDestination(transactionId: Long): NavigationRoute {
        return TransactionEditor(transactionId)
    }

    internal fun transactionDetailDestination(transactionId: Long): NavigationRoute {
        return Detail(transactionId)
    }
}