package com.charmflex.cp.flexiexpensesmanager.features.budget.ui.setting

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.new_transaction.CategorySelectionBottomSheet
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.LockedFeatureButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGModalBottomSheet
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.features.SettingEditorScreen
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BudgetSettingScreen(
    budgetSettingViewModel: BudgetSettingViewModel
) {
    val viewState by budgetSettingViewModel.viewState.collectAsState()
    val bsState = viewState.bottomSheetState
    val bottomSheetState = rememberModalBottomSheetState()
    val currencyVisualTransformation = remember(viewState.currencyCode) {
        budgetSettingViewModel.currencyVisualTransformationBuilder().create(viewState.currencyCode)
    }

    BudgetListScreen(budgetSettingViewModel)

    SGAnimatedTransition(
        isVisible = viewState.isEditorOn,
        enter = slideInHorizontally(initialOffsetX = { it }),
        exit = slideOutHorizontally(targetOffsetX = { it })
    ) {
        SettingEditorScreen(
            fields = viewState.fields,
            appBarTitle = stringResource(Res.string.budget_setting_add_new_budget_app_title),
            onTextFieldChanged = { newValue, field ->
                budgetSettingViewModel.onFieldValueChanged(field, newValue)
            },
            onBack = { budgetSettingViewModel.onBack() },
            onCallBackFieldTap = {
                budgetSettingViewModel.onCallBackAction(it)
            },
            currencyVisualTransformation = currencyVisualTransformation,
            screenName = "BudgetSettingScreen"
        ) {
            budgetSettingViewModel.submitBudget()
        }
    }


    bsState?.let { state ->
        SGModalBottomSheet(
            modifier = Modifier.padding(grid_x2),
            sheetState = bottomSheetState,
            onDismiss = { budgetSettingViewModel.toggleBottomSheet(null) }
        ) {
            when (state) {
                is BudgetSettingViewState.BudgetSettingBottomSheetState.CategoryOption -> {
                    CategorySelectionBottomSheet(
                        onSelected = { id, name ->
                            budgetSettingViewModel.onFieldValueChanged(state.feField, name, id)
                            budgetSettingViewModel.toggleBottomSheet(null)
                        },
                        transactionCategories = viewState.expensesCategories
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetItemList(
    modifier: Modifier = Modifier,
    budgetItems: List<BudgetSettingViewState.BudgetListItem>,
    onDeleteBudget: (BudgetSettingViewState.BudgetListItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        budgetItems.forEach { budgetItem ->
            BudgetItemCard(
                budgetItem = budgetItem,
                onDelete = { onDeleteBudget(budgetItem) }
            )
        }
    }
}

@Composable
private fun BudgetItemCard(
    budgetItem: BudgetSettingViewState.BudgetListItem,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = grid_x0_5, vertical = grid_x0_5)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.4f)
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(grid_x2)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(grid_x2)
        ) {
            // Budget Icon
            Box(
                modifier = Modifier
                    .size(grid_x4)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(grid_x1)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.car_speed_limiter),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(grid_x2)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Category Name
                Text(
                    text = budgetItem.categoryName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                // Budget Label
                Text(
                    text = "Monthly Budget",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Budget Amount
            Text(
                text = budgetItem.budget,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(grid_x4)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_delete),
                    contentDescription = "Delete budget",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(grid_x2_5)
                )
            }
        }
    }
}

@Composable
private fun BudgetListScreen(
    viewModel: BudgetSettingViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    SGScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(grid_x2),
        topBar = {
            BasicTopBar(
                title = stringResource(Res.string.budget_setting_app_bar_title),
                actions =  {
                    if (!viewState.isFeatureEnabled) return@BasicTopBar

                    IconButton(onClick = { viewModel.toggleEditor(true) }) {
                        SGIcons.Add()
                    }
                }
            )
        }
    ) {
        if (!viewState.isFeatureEnabled) {
            NoResultContent(modifier = Modifier.weight(1f), "This feature is only allowed for premium user. Purchase to unlock it!")
            LockedFeatureButton(modifier = Modifier.fillMaxWidth(), "🔒 Unlock Budget Feature") {
                viewModel.navigateToBilling()
            }
            return@SGScaffold
        }

        if (viewState.budgetListItem.isEmpty()) {
            NoResultContent(modifier = Modifier.weight(1f), "No budget is set. Create one?")
        } else {
            BudgetItemList(
                modifier = Modifier.weight(1f),
                budgetItems = viewState.budgetListItem,
                onDeleteBudget = { viewModel.onDeleteBudget(it) }
            )
        }
    }
}