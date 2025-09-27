package com.charmflex.cp.flexiexpensesmanager.features.category.category.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.EditorCard
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLargePrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGTextField
import com.charmflex.cp.flexiexpensesmanager.ui_common.SelectionItem
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.category_add_subcategory_title
import kotlinproject.composeapp.generated.resources.empty_expenses_category
import kotlinproject.composeapp.generated.resources.empty_income_category
import kotlinproject.composeapp.generated.resources.error_find_category_path_failed
import kotlinproject.composeapp.generated.resources.generic_add
import kotlinproject.composeapp.generated.resources.generic_add_new_category
import kotlinproject.composeapp.generated.resources.generic_cancel
import kotlinproject.composeapp.generated.resources.generic_category
import kotlinproject.composeapp.generated.resources.generic_confirm
import kotlinproject.composeapp.generated.resources.generic_delete_warning_subtitle
import kotlinproject.composeapp.generated.resources.generic_new_category
import kotlinproject.composeapp.generated.resources.generic_warning
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CategoryEditorScreen(viewModel: CategoryEditorViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val currentNode = viewState.currentNode
    val title = when (val n = currentNode) {
        null -> stringResource(Res.string.generic_category)
        else -> "${stringResource(Res.string.category_add_subcategory_title)}${n.categoryName}"
    }
    val items = when (val n = currentNode) {
        null -> viewState.categoryChain.categoryTree.items
        else -> n.children
    }
    val isEditorOpened = viewState.editorState.isOpened
    val scrollState = rememberScrollState()
    val snackBarState by viewModel.snackBarState.collectAsState()
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val genericError = stringResource(Res.string.error_find_category_path_failed)

    LaunchedEffect(key1 = snackBarState) {
        when (val state = snackBarState) {
            is SnackBarState.Success -> snackBarHostState.showSnackBarImmediately(state.message)
            is SnackBarState.Error -> snackBarHostState.showSnackBarImmediately(
                state.message ?: genericError
            )

            else -> {}
        }
    }

    BackHandler {
        viewModel.back()
    }

    SGScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(grid_x2),
        topBar = {
            BasicTopBar(title = title)
        },
        screenName = "CategoryEditorScreen"
    ) {

        if (isEditorOpened) {
            EditorCard(
                modifier = Modifier.fillMaxSize(),
                header = stringResource(Res.string.generic_add_new_category),
                buttonText = stringResource(Res.string.generic_add),
                onButtonClicked = { viewModel.addNewCategory() }
            ) {
                SGTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(Res.string.generic_new_category),
                    value = viewState.editorState.value
                ) {
                    viewModel.updateEditorValue(it)
                }
            }
        } else {
            Column {
                if (items.isEmpty()) {
                    val msg = if (viewModel.editorTypeCode == TransactionType.EXPENSES) {
                        stringResource(Res.string.empty_expenses_category)
                    } else {
                        stringResource(Res.string.empty_income_category)
                    }
                    NoResultContent(modifier = Modifier.weight(1f), msg)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = grid_x2)
                    ) {
                        Column(
                            modifier = Modifier.verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(grid_x1)
                        ) {
                            items.forEachIndexed { index, it ->
                                SelectionItem(
                                    item = it,
                                    title = { it.categoryName },
                                    onClick = { viewModel.onClickItem(it) },
                                    onSuffixIconClicked = { viewModel.onClickItem(it) },
                                    onSubIconClick = { viewModel.launchDeleteDialog(it.categoryId) },
                                    subPrefixIcon = { SGIcons.Delete() },
                                    suffixIcon = { if (it.allowSubCategory) SGIcons.NextArrow() },
                                    showDivider = index != 0
                                )
                            }
                        }
                    }
                }


                if (viewState.dialogState != null) SGActionDialog(
                    title = stringResource(Res.string.generic_warning),
                    text = stringResource(Res.string.generic_delete_warning_subtitle),
                    onDismissRequest = viewModel::closeDeleteDialog,
                    primaryButtonText = stringResource(Res.string.generic_confirm),
                    secondaryButtonText = stringResource(Res.string.generic_cancel)
                ) {
                    viewModel.deleteCategory()
                    viewModel.closeDeleteDialog()
                }

                SGLargePrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.generic_add)
                ) {
                    viewModel.openEditor()
                }
            }
        }
    }

    SGSnackBar(
        snackBarHostState = snackBarHostState,
        snackBarType = if (snackBarState is SnackBarState.Error) SnackBarType.Error else SnackBarType.Success
    )
}