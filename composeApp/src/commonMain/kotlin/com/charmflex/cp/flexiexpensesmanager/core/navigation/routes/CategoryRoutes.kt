package com.charmflex.cp.flexiexpensesmanager.core.navigation.routes

import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import kotlinx.serialization.Serializable

internal object CategoryRoutes {
    private const val ROOT = "category"

    object Args {
        const val TRANSACTION_TYPE = "transaction_type"
        const val IMPORT_FIX_CATEGORY_NAME = "import_fix_cat_name"
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "category_name"
        const val CATEGORY_DATE_FILTER = "CATEGORY_DATE_FILTER"
        const val CATEGORY_DATE_FILTER_2 = "CATEGORY_DATE_FILTER_2"
    }
    @Serializable
    data class CategoryEditorDefault(
        val transactionType: TransactionType,
    ) : NavigationRoute
    @Serializable
    data class ImportCategory(
        val transactionType: TransactionType,
        val newCategoryName: String? = null
    ) : NavigationRoute

    @Serializable
    data class CategoryTransactionDetail(
        val categoryId: Int,
        val categoryName: String,
        val transactionType: TransactionType
    ) : NavigationRoute

    @Serializable
    data class Stat(
        val dateFilter: DateFilter
    ) : NavigationRoute

    fun editorDestination(transactionType: TransactionType, newCategoryName: String? = null): NavigationRoute {
        if (newCategoryName == null) {
            return CategoryEditorDefault(
                transactionType,
            )
        } else {
            return ImportCategory(
                transactionType,
                newCategoryName
            )
        }
    }

    fun categoryTransactionDetail(categoryId: Int, categoryName: String, transactionType: TransactionType): NavigationRoute {
        return CategoryTransactionDetail(
            categoryId, categoryName, transactionType
        )
    }
}