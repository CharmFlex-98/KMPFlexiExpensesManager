package com.charmflex.cp.flexiexpensesmanager.features.backup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentPicker
import com.charmflex.cp.flexiexpensesmanager.core.utils.toPercentageString
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FECallout3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.ListTable
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.TransparentBackground
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@OptIn(ExperimentalComposeUiApi::class)
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
        }
    }


    SGScaffold(
        modifier = Modifier.padding(grid_x2),
        screenName = "ImportDataScreen"
    ) {
        if (viewState.importedData.isEmpty()) {
            PreLoadScreen(viewState = viewState) {
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

    if (viewState.isLoading) {
        BackHandler {
            // Do nothing
        }

        TransparentBackground(0.4f) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { viewState.progress },
                    )
                    if (viewState.progress == 0f) FEBody3(
                        modifier = Modifier.padding(grid_x1),
                        text = "Initializing...",
                        color = Color.White
                    ) else FEBody3(
                        modifier = Modifier.padding(grid_x1),
                        text = "${round(viewState.progress * 100)}%", color = Color.White
                    )
                }

            }
        }
    }

    SGSnackBar(snackBarHostState = snackBarHostState, snackBarType = SnackBarType.Error)
}

@Composable
private fun ColumnScope.PreLoadScreen(
    viewState: ImportDataViewState,
    onPicked: (ByteArray?) -> Unit
) {
    var show by remember { mutableStateOf(false) }

    DocumentPicker(
        show = show,
        onPicked
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        FEBody2(text = "Click to load data")
    }
    SGLargePrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(grid_x2), text = "Import"
    ) {
        show = true
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

    TabRow(selectedTabIndex = tabIndex) {
        tabs.forEachIndexed { index, tabName ->
            Tab(selected = index == tabIndex,
                onClick = { onUpdateTabIndex(index) }
            ) {
                FEHeading4(modifier = Modifier.padding(grid_x2), text = tabName)
            }
        }
    }
    when (tabIndex) {
        0 -> {
            ListTable(
                modifier = Modifier.weight(1f),
                items = viewState.importedData
            ) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(grid_x1)
                ) {
                    Row {
                        Box(
                            modifier = Modifier.padding(grid_x1),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${index + 1})")
                        }
                        Column {
                            FEBody3(text = item.transactionName)
                            when (val it = item.categoryColumns) {
                                is ImportedData.RequiredDataState.Missing -> {
                                    MissingItemText(
                                        entityName = "category",
                                        entityItemName = it.name
                                    )
                                }

                                is ImportedData.RequiredDataState.Acquired -> {
                                    AcquiredItemText(
                                        entityName = "category",
                                        entityItemName = it.name
                                    )
                                }

                                else -> {}
                            }
                            item.accountFrom?.let {
                                when (it) {
                                    is ImportedData.RequiredDataState.Missing -> {
                                        MissingItemText(
                                            entityName = "account from",
                                            entityItemName = it.name
                                        )
                                    }

                                    is ImportedData.RequiredDataState.Acquired -> {
                                        AcquiredItemText(
                                            entityName = "account from",
                                            entityItemName = it.name
                                        )
                                    }
                                }
                            }

                            item.accountTo?.let {
                                when (it) {
                                    is ImportedData.RequiredDataState.Missing -> {
                                        MissingItemText(
                                            entityName = "account to",
                                            entityItemName = it.name
                                        )
                                    }

                                    is ImportedData.RequiredDataState.Acquired -> {
                                        AcquiredItemText(
                                            entityName = "account to",
                                            entityItemName = it.name
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SGLargePrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = grid_x1),
                text = stringResource(Res.string.backup_save_data_button),
                enabled = viewState.missingData.isEmpty()
            ) {
                onSave()
            }
        }

        1 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(grid_x2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FECallout3(text = stringResource(Res.string.error_fix_progress_bar_label))
                androidx.compose.material3.LinearProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = grid_x1),
                    progress = { viewState.importFixPercentage }
                )
                FECallout3(text = toPercentageString(viewState.importFixPercentage))
            }
            ListTable(items = viewState.missingData.toList()) { index, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .clickable {
                            onFixError(item)
                        }
                        .padding(grid_x2)
                ) {
                    when (item.dataType) {
                        ImportedData.MissingData.DataType.ACCOUNT_FROM, ImportedData.MissingData.DataType.ACCOUNT_TO -> {
                            val note = when (item.dataType) {
                                ImportedData.MissingData.DataType.ACCOUNT_FROM -> stringResource(Res.string.import_account_error_text_note_account_from)
                                ImportedData.MissingData.DataType.ACCOUNT_TO -> stringResource(Res.string.import_account_error_text_note_account_to)
                                else -> ""
                            }
                            FEBody2(text = "Create account: ${item.name} $note")
                        }

                        ImportedData.MissingData.DataType.EXPENSES_CATEGORY -> {
                            FEBody2(text = "Create expenses category: ${item.name}")
                        }

                        ImportedData.MissingData.DataType.INCOME_CATEGORY -> {
                            FEBody2(text = "Create income category: ${item.name}")
                        }

                        ImportedData.MissingData.DataType.TAG -> {
                            FEBody2(text = "Create tag: ${item.name}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MissingItemText(
    entityName: String,
    entityItemName: String
) {
    FEMetaData1(text = "$entityName: $entityItemName is missing!", color = Color.Red)
}

@Composable
private fun AcquiredItemText(
    entityName: String,
    entityItemName: String
) {
    FEMetaData1(text = "$entityName: $entityItemName")
}