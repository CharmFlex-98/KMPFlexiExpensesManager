package com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository

import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model.UserInfo

internal interface AuthRepository {
    suspend fun upsertUser(
        uid: String,
        username: String? = null,
        email: String? = null
    ): UserInfo
}