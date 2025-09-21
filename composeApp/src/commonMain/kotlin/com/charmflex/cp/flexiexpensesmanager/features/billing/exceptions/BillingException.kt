package com.charmflex.cp.flexiexpensesmanager.features.billing.exceptions

sealed interface BillingException

object NetworkError : BillingException, Throwable()