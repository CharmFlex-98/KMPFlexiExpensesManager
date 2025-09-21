package com.charmflex.cp.flexiexpensesmanager.features.home.ui.setting

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x6
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingScreen(
    viewModel: SettingViewModel,
    onRefresh: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()
    val snackbarState by viewModel.snackBarState
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarType = remember(snackbarState) {
        when (snackbarState) {
            is SnackBarState.Success -> SnackBarType.Success
            is SnackBarState.Error -> SnackBarType.Error
            else -> null
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.onDataClearedEvent.collectLatest {
            when (it) {
                OnDataCleared.Refresh -> onRefresh()
                OnDataCleared.Finish -> {} // (localContext as? Activity)?.finish()
            }
        }
    }

    LaunchedEffect(key1 = snackbarState) {
        when (val state = snackbarState) {
            is SnackBarState.Error -> {
                snackbarHostState.showSnackBarImmediately(state.message ?: "Something went wrong")
                viewModel.refreshSnackBarState()
            }

            is SnackBarState.Success -> {
                snackbarHostState.showSnackBarImmediately(state.message)
                viewModel.refreshSnackBarState()
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = grid_x2),
        verticalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        // Settings Items
        viewState.items.forEach { settingItem ->
            SettingItemCard(
                title = settingItem.title,
                iconRes = getIconForSettingAction(settingItem.action),
                onClick = { viewModel.onTap(settingItem.action) }
            )
        }
    }

    viewState.dialogState?.let {
        SGDialog(
            title = stringResource(it.title),
            subtitle = stringResource(it.subtitle),
            onDismissRequest = viewModel::closeDialog
        ) {
            when (it) {
                is SettingDialogState.ResetDataDialogState -> {
                    ResetDataDialogContentSelection(
                        resetDataDialogState = it,
                        viewModel::toggleResetSelection
                    ) {
                        viewModel.onFactoryResetConfirmed()
                    }
                }
            }
        }
    }

    if (viewState.isLoading) Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 3.dp
        )
    }

    snackbarType?.let {
        SGSnackBar(snackBarHostState = snackbarHostState, snackBarType = it)
    }
}

@Composable
private fun SettingItemCard(
    title: String,
    iconRes: DrawableResource,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.6f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(grid_x2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icon with background circle
                Box(
                    modifier = Modifier
                        .size(grid_x6)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(grid_x1_5)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(grid_x3)
                    )
                }
                
                Spacer(modifier = Modifier.width(grid_x2))
                
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            // Chevron arrow using your existing arrow icon
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_next),
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(grid_x2_5)
            )
        }
    }
}

// Function to map setting actions to appropriate drawable resource IDs
@Composable
private fun getIconForSettingAction(action: SettingAction): DrawableResource {
    return when (action) {
        // Categories
        SettingAction.EXPENSES_CAT -> Res.drawable.cash_multiple
        SettingAction.INCOME_CAT -> Res.drawable.cash_multiple
        
        // Account & Financial
        SettingAction.ACCOUNT -> Res.drawable.bank
        SettingAction.PRIMARY_CURRENCY -> Res.drawable.currency_sign
        SettingAction.SECONDARY_CURRENCY -> Res.drawable.currency_sign
        
        // Data Management
        SettingAction.Export -> Res.drawable.database_export_outline
        SettingAction.Import -> Res.drawable.database_import_outline
        SettingAction.RESET_DATA -> Res.drawable.cog_refresh_outline
        
        // Features
        SettingAction.Tag -> Res.drawable.tag_multiple_outline
        SettingAction.SCHEDULER -> Res.drawable.cash_clock
        SettingAction.BUDGET -> Res.drawable.car_speed_limiter

        SettingAction.BILLING -> Res.drawable.add_shopping_cart
    }
}

@Composable
private fun ResetDataDialogContentSelection(
    resetDataDialogState: SettingDialogState.ResetDataDialogState,
    onToggleSelection: (SettingDialogState.ResetDataDialogState.ResetType) -> Unit,
    onFactoryResetConfirmed: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        // Header with warning message
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(grid_x1)
                )
                .padding(grid_x1_5),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_info),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(grid_x2_5)
            )
            Spacer(modifier = Modifier.width(grid_x1))
            Text(
                text = "This action cannot be undone",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.error
            )
        }
        
        Spacer(modifier = Modifier.height(grid_x1))
        
        // Reset type selection
        Text(
            text = "Select what to reset:",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            SettingDialogState.ResetDataDialogState.ResetType.entries.forEach { resetType ->
                ResetOptionCard(
                    resetType = resetType,
                    isSelected = resetDataDialogState.selection == resetType,
                    onSelect = { onToggleSelection(resetType) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(grid_x2))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            // Cancel button (implicit through dialog dismiss)
            SGLargePrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.generic_confirm),
                enabled = resetDataDialogState.selection != null
            ) {
                onFactoryResetConfirmed()
            }
        }
    }
}

@Composable
private fun ResetOptionCard(
    resetType: SettingDialogState.ResetDataDialogState.ResetType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val (title, description, icon) = getResetTypeInfo(resetType)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        shape = RoundedCornerShape(grid_x1_5),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(grid_x5)
                    .background(
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(grid_x1)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(grid_x2_5)
                )
            }
            
            Spacer(modifier = Modifier.width(grid_x1_5))
            
            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            // Selection indicator
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = androidx.compose.material3.RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun getResetTypeInfo(resetType: SettingDialogState.ResetDataDialogState.ResetType): Triple<String, String, DrawableResource> {
    return Triple(
        resetType.value,
        "Reset ${resetType.value.lowercase()} data",
        Res.drawable.cog_refresh_outline
    )
}