package com.charmflex.cp.flexiexpensesmanager.core.utils.ui

import androidx.savedstate.SavedState
import androidx.savedstate.read

internal fun SavedState?.getString(key: String): String? {
    return this?.read { getString(key) }
}

internal fun SavedState?.getInt(key: String): Int? {
    return this?.read { getInt(key) }
}

internal fun SavedState?.getLong(key: String): Long? {
    return this?.read { getLong(key) }
}