package com.charmflex.cp.flexiexpensesmanager.features.auth.service.sign_in
import com.charmflex.flexiexpensesmanager.features.auth.service.sign_in.SignInState

interface SignInService {
    suspend fun signIn(): SignInState
    suspend fun trySignIn(): SignInState
}