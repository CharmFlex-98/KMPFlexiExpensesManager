package com.charmflex.cp.flexiexpensesmanager.core.crypto


internal interface SignatureVerifier {
    fun verify(payload: String, signature: String): Boolean
}