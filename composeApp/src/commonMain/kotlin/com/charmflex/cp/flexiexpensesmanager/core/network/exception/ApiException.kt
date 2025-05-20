package com.charmflex.cp.flexiexpensesmanager.core.network.exception

internal data class ApiException(
    val errorCode: Int,
    val errorMessage: String
) : Exception(errorMessage)