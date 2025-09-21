package com.charmflex.cp.flexiexpensesmanager.features.backup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentPicker
import com.charmflex.cp.flexiexpensesmanager.core.utils.toPercentageString
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.LockedFeatureButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.LockedScreen
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.TransparentBackground
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x6
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ImportDataScreen(importDataViewModel: ImportDataViewModel) {

    val viewState by importDataViewModel.viewState.collectAsState()
    val tabIndex by importDataViewModel.tabIndex.collectAsState()
    val errorString by importDataViewModel.snackBarState.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(key1 = errorString) {
        if (errorString.isNotBlank()) {
            snackBarHostState.showSnackBarImmediately(errorString)
            importDataViewModel.resetSnackbarState()
        }
    }

    SGScaffold(
        modifier = Modifier.padding(grid_x2),
        isLoading = viewState.isLoading,
        screenName = "ImportDataScreen",
        topBar = {
            BasicTopBar(title = "Import Data")
        }
    ) {
        if (viewState.importedData.isEmpty()) {
            PreLoadScreen(
                viewModel = importDataViewModel,
                isFeatureUnlocked = viewState.isFeatureEnabled
            ) {
                importDataViewModel.importData(it)
            }
        } else {
            LoadedScreen(
                tabIndex = tabIndex,
                importDataViewModel::updateTabIndex,
                viewState = viewState,
                onFixError = {
                    importDataViewModel.onFixError(it)
                },
            ) {
                importDataViewModel.saveData()
            }
        }
    }

    if (viewState.isImportLoading) {
        BackHandler {
            // Do nothing
        }

        TransparentBackground(0.4f) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingProgressCard(
                    progress = viewState.progress
                )
            }
        }
    }

    SGSnackBar(snackBarHostState = snackBarHostState, snackBarType = SnackBarType.Error)
}

@Composable
private fun LoadingProgressCard(progress: Float) {
    Card(
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.8f)
            ),
        shape = RoundedCornerShape(grid_x2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(grid_x3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(grid_x2)
        ) {
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
                    painter = painterResource(Res.drawable.database_import_outline),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(grid_x3)
                )
            }

            Text(
                text = "Importing Data",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.width(200.dp),
                gapSize = 0.dp,
                drawStopIndicator = {}
            )

            Text(
                text = if (progress == 0f) "Initializing..." else "${round(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ColumnScope.PreLoadScreen(
    viewModel: ImportDataViewModel,
    isFeatureUnlocked: Boolean,
    onPicked: (ByteArray?) -> Unit
) {
    var show by remember { mutableStateOf(false) }

    DocumentPicker(
        show = show,
    ) {
        show = false
        onPicked(it)
    }

    LockedScreen(
        isFeatureUnlocked,
        unlockedTitle = "Ready to import file",
        unlockedSubtitle = "Select a backup file to restore your transaction",
        unlockedDrawableRes = Res.drawable.database_import_outline
    )

    if (isFeatureUnlocked) {
        SGLargePrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Select Backup File"
        ) {
            show = true
        }
    } else {
        LockedFeatureButton(
            modifier = Modifier.fillMaxWidth(),
            text = "🔒 Unlock Import Feature",
            onClick = {
                viewModel.purchaseIAP()
            }
        )
    }
}

@Composable
private fun ColumnScope.LoadedScreen(
    tabIndex: Int,
    onUpdateTabIndex: (Int) -> Unit,
    viewState: ImportDataViewState,
    onFixError: (ImportedData.MissingData) -> Unit,
    onSave: () -> Unit
) {
    val tabs = listOf(
        stringResource(Res.string.error_fix_tab_label_all_data),
        stringResource(Res.string.error_fix_tab_label_errors)
    )

    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        tabs.forEachIndexed { index, tabName ->
            Tab(
                selected = index == tabIndex,
                onClick = { onUpdateTabIndex(index) }
            ) {
                Text(
                    text = tabName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = if (index == tabIndex) FontWeight.SemiBold else FontWeight.Medium
                    ),
                    modifier = Modifier.padding(grid_x2)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(grid_x2))

    when (tabIndex) {
        0 -> AllDataTab(
            modifier = Modifier.weight(1f),
            viewState = viewState,
            onSave = onSave
        )

        1 -> ErrorsTab(
            modifier = Modifier.weight(1f),
            viewState = viewState,
            onFixError = onFixError
        )
    }
}

@Composable
private fun AllDataTab(
    modifier: Modifier = Modifier,
    viewState: ImportDataViewState,
    onSave: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(grid_x2)
    ) {
        // Import Progress Section
        ImportProgressSection(
            totalItems = viewState.importedData.size,
            errorCount = viewState.missingData.size,
            fixPercentage = viewState.importFixPercentage
        )

        // Transactions Header
        Text(
            text = "Transaction Data (${viewState.importedData.size})",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        // Transaction List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            viewState.importedData.forEachIndexed { index, item ->
                TransactionImportRow(
                    index = index + 1,
                    item = item
                )
            }
        }

        // Save Button
        SGLargePrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.backup_save_data_button),
            enabled = viewState.missingData.isEmpty()
        ) {
            onSave()
        }
    }
}

@Composable
private fun ErrorsTab(
    modifier: Modifier = Modifier,
    viewState: ImportDataViewState,
    onFixError: (ImportedData.MissingData) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(grid_x2)
    ) {
        // Import Progress Section
        ImportProgressSection(
            totalItems = viewState.importedData.size,
            errorCount = viewState.missingData.size,
            fixPercentage = viewState.importFixPercentage
        )

        // Errors Header
        Text(
            text = "Errors to Fix (${viewState.missingData.size})",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        // Errors List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            viewState.missingData.forEach { item ->
                ErrorItemRow(
                    item = item,
                    onClick = { onFixError(item) }
                )
            }
        }
    }
}

@Composable
private fun ImportProgressSection(
    totalItems: Int,
    errorCount: Int,
    fixPercentage: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(grid_x2),
                spotColor = Color.Black.copy(alpha = 0.6f)
            ),
        shape = RoundedCornerShape(grid_x2),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(grid_x2),
            verticalArrangement = Arrangement.spacedBy(grid_x2)
        ) {
            // Header
            Text(
                text = "Import Summary",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    label = "Total Items",
                    value = totalItems.toString(),
                    iconRes = Res.drawable.database_import_outline,
                    color = MaterialTheme.colorScheme.primary
                )

                StatItem(
                    label = "Errors",
                    value = errorCount.toString(),
                    iconRes = Res.drawable.delete_alert_outline,
                    color = if (errorCount > 0) Color(0xFFF44336) else Color(0xFF4CAF50)
                )
            }

            // Progress Bar (only show if there are errors to fix)
            if (errorCount > 0) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(grid_x1)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fix Progress",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = toPercentageString(fixPercentage),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    LinearProgressIndicator(
                        progress = { fixPercentage },
                        modifier = Modifier.fillMaxWidth(),
                        gapSize = 0.dp,
                        drawStopIndicator = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(grid_x1_5)
    ) {
        Box(
            modifier = Modifier
                .size(grid_x5)
                .background(
                    color = color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(grid_x1)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(grid_x2_5)
            )
        }

        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransactionImportRow(
    index: Int,
    item: ImportedData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = grid_x1, horizontal = grid_x0_5),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(grid_x2)
    ) {
        // Index Circle - more subtle
        Box(
            modifier = Modifier
                .size(grid_x3)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50) // Perfect circle
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(grid_x1)
        ) {
            // Transaction Name
            Text(
                text = item.transactionName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Status Details - using DetailRow pattern
            Column(
                verticalArrangement = Arrangement.spacedBy(grid_x0_5)
            ) {
                // Category status
                when (val categoryState = item.categoryColumns) {
                    is ImportedData.RequiredDataState.Missing -> {
                        StatusDetailRow(
                            label = "Category",
                            value = categoryState.name,
                            isError = true,
                            iconRes = Res.drawable.tag_multiple_outline
                        )
                    }

                    is ImportedData.RequiredDataState.Acquired -> {
                        StatusDetailRow(
                            label = "Category",
                            value = categoryState.name,
                            isError = false,
                            iconRes = Res.drawable.tag_multiple_outline
                        )
                    }

                    else -> {}
                }

                // Account from status
                item.accountFrom?.let { accountState ->
                    when (accountState) {
                        is ImportedData.RequiredDataState.Missing -> {
                            StatusDetailRow(
                                label = "From",
                                value = accountState.name,
                                isError = true,
                                iconRes = Res.drawable.bank
                            )
                        }

                        is ImportedData.RequiredDataState.Acquired -> {
                            StatusDetailRow(
                                label = "From",
                                value = accountState.name,
                                isError = false,
                                iconRes = Res.drawable.bank
                            )
                        }
                    }
                }

                // Account to status
                item.accountTo?.let { accountState ->
                    when (accountState) {
                        is ImportedData.RequiredDataState.Missing -> {
                            StatusDetailRow(
                                label = "To",
                                value = accountState.name,
                                isError = true,
                                iconRes = Res.drawable.bank
                            )
                        }

                        is ImportedData.RequiredDataState.Acquired -> {
                            StatusDetailRow(
                                label = "To",
                                value = accountState.name,
                                isError = false,
                                iconRes = Res.drawable.bank
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusDetailRow(
    label: String,
    value: String,
    isError: Boolean,
    iconRes: org.jetbrains.compose.resources.DrawableResource
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        // Small status icon
        Box(
            modifier = Modifier
                .size(grid_x2_5)
                .background(
                    color = if (isError) {
                        Color(0xFFF44336).copy(alpha = 0.1f)
                    } else {
                        Color(0xFF4CAF50).copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(grid_x0_5)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (isError) Color(0xFFF44336) else Color(0xFF4CAF50),
                modifier = Modifier.size(grid_x1_5)
            )
        }

        // Status text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FEMetaData1(
                text = "$label:",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (isError) FontWeight.Medium else FontWeight.Normal
                ),
                color = if (isError) {
                    Color(0xFFF44336)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun StatusChip(
    text: String,
    isError: Boolean
) {
    val (backgroundColor, textColor) = if (isError) {
        Color(0xFFF44336).copy(alpha = 0.1f) to Color(0xFFF44336)
    } else {
        Color(0xFF4CAF50).copy(alpha = 0.1f) to Color(0xFF4CAF50)
    }

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(grid_x0_5)
            )
            .padding(horizontal = grid_x1, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
private fun ErrorItemRow(
    item: ImportedData.MissingData,
    onClick: () -> Unit
) {
    val (title, subtitle, iconRes) = when (item.dataType) {
        ImportedData.MissingData.DataType.ACCOUNT_FROM -> {
            Triple(
                "Create Account (From)",
                "Account for withdrawals: ${item.name}",
                Res.drawable.bank
            )
        }

        ImportedData.MissingData.DataType.ACCOUNT_TO -> {
            Triple("Create Account (To)", "Account for deposits: ${item.name}", Res.drawable.bank)
        }

        ImportedData.MissingData.DataType.EXPENSES_CATEGORY -> {
            Triple(
                "Create Expense Category",
                "Category: ${item.name}",
                Res.drawable.tag_multiple_outline
            )
        }

        ImportedData.MissingData.DataType.INCOME_CATEGORY -> {
            Triple(
                "Create Income Category",
                "Category: ${item.name}",
                Res.drawable.tag_multiple_outline
            )
        }

        ImportedData.MissingData.DataType.TAG -> {
            Triple("Create Tag", "Tag: ${item.name}", Res.drawable.tag_multiple_outline)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
                shape = RoundedCornerShape(grid_x1_5)
            )
            .clickable { onClick() }
            .padding(grid_x2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(grid_x2)
    ) {
        // Error Icon
        Box(
            modifier = Modifier
                .size(grid_x4)
                .background(
                    color = Color(0xFFF44336).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(grid_x0_5)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color(0xFFF44336),
                modifier = Modifier.size(grid_x2)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Arrow Icon
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_next),
            contentDescription = "Fix error",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(grid_x2_5)
        )
    }
}
