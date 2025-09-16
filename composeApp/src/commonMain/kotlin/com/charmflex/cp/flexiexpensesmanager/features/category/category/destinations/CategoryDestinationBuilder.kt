package com.charmflex.cp.flexiexpensesmanager.features.category.category.destinations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromEnd
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalEnterFromStart
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEHorizontalExitToEnd
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEVerticalSlideDown
import com.charmflex.cp.flexiexpensesmanager.core.navigation.FEVerticalSlideUp
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getInt
import com.charmflex.cp.flexiexpensesmanager.core.utils.ui.getString
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.CategoryRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.TagRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilterNavType
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.CategoryEditorScreen
import com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.CategoryEditorViewModel
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
            val route = it.toRoute<CategoryRoutes.CategoryEditorDefault>()
            getEditorScreen(route)
        }
    }

    private fun NavGraphBuilder.importEditorScreen() {
        composable<CategoryRoutes.ImportCategory>(
            enterTransition = FEVerticalSlideUp,
            exitTransition = FEVerticalSlideDown
        ) {
            val route = it.toRoute<CategoryRoutes.ImportCategory>()
            getEditorScreen(route)
        }
    }

    @Composable
    private fun getEditorScreen(route: CategoryRoutes.CategoryEditorRoute) {
        val viewModel = when (route) {
            is CategoryRoutes.ImportCategory -> {
                getViewModel {
                    appComponent.categoryEditorViewModel()
                        .apply { setType(type = route.transactionType.name, route.newCategoryName) }
                }
            }

            is CategoryRoutes.CategoryEditorDefault -> {
                getViewModel {
                    appComponent.categoryEditorViewModel()
                        .apply { setType(type = route.transactionType.name, null) }
                }
            }
        }
        CategoryEditorScreen(viewModel = viewModel)
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
            val route = it.toRoute<CategoryRoutes.Stat>()
            val dateFilter = route.dateFilter

            val viewModel = getViewModel {
                appComponent.categoryStatViewModel()
                    .apply { onDateFilterChanged(dateFilter) }
            }
            CategoryStatScreen(viewModel = viewModel)
        }
    }

    private fun NavGraphBuilder.categoryTransactionDetailScreen() {
        composable<CategoryRoutes.CategoryTransactionDetail>(
            enterTransition = FEHorizontalEnterFromEnd,
            popExitTransition = FEHorizontalExitToEnd,
            popEnterTransition = FEHorizontalEnterFromStart,
            typeMap = mapOf(
                typeOf<DateFilter>() to DateFilterNavType
            )
        ) {

            val route = it.toRoute<CategoryRoutes.CategoryTransactionDetail>()

            val dateFilter = route.dateFilter
            val categoryId = route.categoryId
            val categoryName = route.categoryName
            val transactionType = route.transactionType
            val viewModel = getViewModel {
                appComponent.categoryDetailViewModelFactory()
                    .create(categoryId, categoryName, transactionType, dateFilter)
            }
            CategoryDetailScreen(viewModel = viewModel)
        }
    }

}