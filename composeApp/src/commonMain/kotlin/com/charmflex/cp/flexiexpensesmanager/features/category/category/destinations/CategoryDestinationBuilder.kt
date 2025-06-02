package com.charmflex.cp.flexiexpensesmanager.features.category.category.destinations

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromEnd
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromStart
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalExitToEnd
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEVerticalSlideDown
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEVerticalSlideUp
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getInt
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getString
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilterNavType
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.CategoryEditorScreen
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.detail.CategoryDetailScreen
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.stat.CategoryStatScreen
import kotlin.reflect.typeOf

internal class CategoryDestinationBuilder(
    private val navController: NavController
) : DestinationBuilder {
    private val appComponent by lazy { AppComponentProvider.instance.getAppComponent() }

    override fun NavGraphBuilder.buildGraph() {
        editorScreen()
        importEditorScreen()
        categoryStatScreen()
        categoryTransactionDetailScreen()
    }

    private fun NavGraphBuilder.editorScreen() {
        composable<CategoryRoutes.CategoryEditorDefault>(
            enterTransition = FEVerticalSlideUp,
            exitTransition = FEVerticalSlideDown
        ) {
            val importFixCatName =
                it.arguments?.getString(CategoryRoutes.Args.IMPORT_FIX_CATEGORY_NAME)
            val type = it.arguments?.getString(CategoryRoutes.Args.TRANSACTION_TYPE).orEmpty()
            val viewModel = getViewModel {
                appComponent.categoryEditorViewModel
                    .apply { setType(type = type, importFixCatName) }
            }

            CategoryEditorScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.importEditorScreen() {
        composable<CategoryRoutes.ImportCategory>(
            enterTransition = FEVerticalSlideUp,
            exitTransition = FEVerticalSlideDown
        ) {
            val importFixCatName =
                it.arguments?.getString(CategoryRoutes.Args.IMPORT_FIX_CATEGORY_NAME)
            val type = it.arguments?.getString(CategoryRoutes.Args.TRANSACTION_TYPE).orEmpty()
            val viewModel = getViewModel {
                appComponent.categoryEditorViewModel
                    .apply { setType(type = type, importFixCatName) }
            }

            CategoryEditorScreen(viewModel = viewModel)
        }
    }


    private fun NavGraphBuilder.categoryStatScreen() {
        composable<CategoryRoutes.Stat>(
            enterTransition = FEHorizontalEnterFromEnd,
            popExitTransition = FEHorizontalExitToEnd,
            popEnterTransition = FEHorizontalEnterFromStart,
            typeMap = mapOf(
                typeOf<DateFilter>() to DateFilterNavType
            )
        ) {
            val dateFilter = remember {
                navController.previousBackStackEntry?.savedStateHandle?.remove<DateFilter>(
                    CategoryRoutes.Args.CATEGORY_DATE_FILTER
                )
            }

            val viewModel = getViewModel {
                appComponent.categoryStatViewModel
                    .apply { dateFilter?.let { onDateFilterChanged(dateFilter) } }
            }
            CategoryStatScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.categoryTransactionDetailScreen() {
        composable<CategoryRoutes.CategoryTransactionDetail>(
            enterTransition = FEHorizontalEnterFromEnd,
            popExitTransition = FEHorizontalExitToEnd,
            popEnterTransition = FEHorizontalEnterFromStart,
        ) {
            val dateFilter = remember {
                navController.previousBackStackEntry?.savedStateHandle?.remove<DateFilter>(
                    CategoryRoutes.Args.CATEGORY_DATE_FILTER
                )
            }

            val categoryId = it.arguments?.getInt(CategoryRoutes.Args.CATEGORY_ID) ?: -1
            val categoryName = it.arguments?.getString(CategoryRoutes.Args.CATEGORY_NAME) ?: ""
            val transactionType =
                it.arguments?.getString(CategoryRoutes.Args.TRANSACTION_TYPE) ?: ""
            val viewModel = getViewModel {
                appComponent.categoryDetailViewModelFactory
                    .create(categoryId, categoryName, transactionType, dateFilter)
            }
            CategoryDetailScreen(viewModel = viewModel)
        }
    }

}