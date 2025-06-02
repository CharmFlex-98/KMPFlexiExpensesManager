package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

internal object TagRoutes {
    object Args {
        const val IMPORT_FIX_TAG_NAME = "import_fix_tag_name"
    }

    @Serializable
    object TagEditorDefault : NavigationRoute
    @Serializable
    data class ImportTagEditor(
        val tagName: String
    ) : NavigationRoute


    fun addNewTagDestination(isImportFix: Boolean = false, newTagValue: String? = null): NavigationRoute {
        return if (isImportFix && newTagValue != null) {
            ImportTagEditor(newTagValue)
        } else {
            TagEditorDefault
        }
    }
}