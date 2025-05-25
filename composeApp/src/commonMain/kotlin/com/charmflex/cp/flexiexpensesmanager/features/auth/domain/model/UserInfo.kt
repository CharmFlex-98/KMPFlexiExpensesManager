package com.charmflex.cp.flexiexpensesmanager.features.auth.domain.model

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal data class UserInfo @OptIn(ExperimentalTime::class) constructor(
    val uid: String,
    val username: String? = null,
    val email: String? = null,
    val createdAt: Instant? = null,
    val isNewUser: Boolean
)