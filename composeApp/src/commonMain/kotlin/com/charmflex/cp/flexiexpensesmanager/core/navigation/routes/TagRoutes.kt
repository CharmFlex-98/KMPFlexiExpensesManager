package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import kotlinx.serialization.Serializable

internal object TagRoutes {
    object Args {
        const val IMPORT_FIX_TAG_NAME = "import_fix_tag_name"
    }

    internal sealed interface TagEditorRoute

    @Serializable
    object TagEditorDefault : NavigationRoute, TagEditorRoute
    @Serializable
    data class ImportTagEditor(
        val tagName: String
    ) : NavigationRoute, TagEditorRoute


    fun addNewTagDestination(isImportFix: Boolean = false, newTagValue: String? = null): NavigationRoute {
        return if (isImportFix && newTagValue != null) {
            ImportTagEditor(newTagValue)
        } else {
            TagEditorDefault
        }
    }
}