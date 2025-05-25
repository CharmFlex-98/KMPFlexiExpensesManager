package com.charmflex.cp.flexiexpensesmanager.core.utils

import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
internal class ResourcesProvider(
) {
    fun getString(resId: StringResource): String {
        return runBlocking {
            org.jetbrains.compose.resources.getString(resId)
        }
    }
}