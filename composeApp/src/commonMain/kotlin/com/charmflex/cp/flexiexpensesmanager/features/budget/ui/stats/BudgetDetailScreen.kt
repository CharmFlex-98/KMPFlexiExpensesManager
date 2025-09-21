package com.charmflex.cp.flexiexpensesmanager.features.budget.ui.stats

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.core.utils.DateFilter
import com.charmflex.cp.flexiexpensesmanager.core.utils.toPercentageString
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.DateFilterBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.DateFilterConfig
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BudgetDetailScreen(
    viewModel: BudgetDetailViewModel
) {
    val viewState by viewModel.viewState.collectAsState()
    val items = viewState.onScreenBudgetSections
    val dateFilter by viewModel.dateFilter.collectAsState()

    SGScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BasicTopBar(title = stringResource(Res.string.budget_detail_app_bar_title))
        },
        screenName = "BudgetDetailScreen"
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = grid_x2, vertical = grid_x1)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            // Date Filter Section
            DateFilterBar(
                modifier = Modifier.padding(vertical = grid_x1),
                currentDateFilter = dateFilter,
                dateFilterConfig = DateFilterConfig(dateFilterOptions = listOf(DateFilter.Monthly::class)),
                onDateFilterChanged = viewModel::onDateFilterChanged
            )

            // Budget Items
            items.forEach { section ->
                section.contents.forEach { budgetItem ->
                    BudgetItemCard(
                        budgetItem = budgetItem,
                        isExpanded = viewState.isItemExpanded(budgetItem),
                        onToggleExpanded = { viewModel.onToggleExpandable(budgetItem) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetItemCard(
    budgetItem: BudgetStatViewState.CategoryBudgetItem,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearEasing
                )
            )
            .let { modifier ->
                if (budgetItem.expandable) {
                    modifier.clickable { onToggleExpanded() }
                } else {
                    modifier
                }
            },
        shape = RoundedCornerShape(grid_x2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2)
        ) {
            // Header Row with Category Name and Budget
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Category Level Indicator
                    Box(
                        modifier = Modifier
                            .size(
                                when (budgetItem.level) {
                                    BudgetStatViewState.CategoryBudgetItem.Level.FIRST -> grid_x3
                                    BudgetStatViewState.CategoryBudgetItem.Level.SECOND -> grid_x2_5
                                    BudgetStatViewState.CategoryBudgetItem.Level.THIRD -> grid_x2
                                }
                            )
                            .background(
                                color = when (budgetItem.level) {
                                    BudgetStatViewState.CategoryBudgetItem.Level.FIRST -> MaterialTheme.colorScheme.primary
                                    BudgetStatViewState.CategoryBudgetItem.Level.SECOND -> MaterialTheme.colorScheme.secondary
                                    BudgetStatViewState.CategoryBudgetItem.Level.THIRD -> MaterialTheme.colorScheme.tertiary
                                },
                                shape = RoundedCornerShape(grid_x1)
                            )
                    )

                    Spacer(modifier = Modifier.width(grid_x1_5))

                    // Category Name
                    Text(
                        text = budgetItem.categoryName,
                        style = when (budgetItem.level) {
                            BudgetStatViewState.CategoryBudgetItem.Level.FIRST -> MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            BudgetStatViewState.CategoryBudgetItem.Level.SECOND -> MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )

                            BudgetStatViewState.CategoryBudgetItem.Level.THIRD -> MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        },
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Budget Amount
                Text(
                    text = "${stringResource(Res.string.generic_budget)}: ${budgetItem.budget}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Expand/Collapse Button
                if (budgetItem.expandable) {
                    IconButton(onClick = onToggleExpanded) {
                        if (isExpanded) {
                            SGIcons.Collapse(modifier = Modifier.size(grid_x2_5))
                        } else {
                            SGIcons.Expand(modifier = Modifier.size(grid_x2_5))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(grid_x1_5))

            // Progress Section
            Column {
                // Progress Label and Amount Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.generic_spend),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = budgetItem.expensesAmount,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "(${toPercentageString(budgetItem.expensesBudgetRatio)})",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (budgetItem.expensesBudgetRatio > 1) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(grid_x1))

                // Progress Bar
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { budgetItem.expensesBudgetRatio.coerceAtMost(1f) },
                    color = if (budgetItem.expensesBudgetRatio > 1) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    gapSize = 0.dp,
                    drawStopIndicator = {}
                )

                // Over-budget indicator
                if (budgetItem.expensesBudgetRatio > 1) {
                    Spacer(modifier = Modifier.height(grid_x1))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SGIcons.Info(
                            modifier = Modifier.size(grid_x2)
                        )
                        Spacer(modifier = Modifier.width(grid_x1))
                        Text(
                            text = "Over budget",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}