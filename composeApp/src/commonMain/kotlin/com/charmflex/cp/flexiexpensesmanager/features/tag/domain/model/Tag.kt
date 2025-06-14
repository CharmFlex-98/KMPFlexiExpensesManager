package com.charmflex.cp.flexiexpensesmanager.features.tag.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Tag(
    val id: Int,
    val name: String
)