package com.charmflex.cp.flexiexpensesmanager.features.utils.redirect

internal interface Redirector {
    fun redirectTo(redirectPath: RedirectPath)
}

internal enum class RedirectPath {
    OFFICIAL_STORE
}