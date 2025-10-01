package com.charmflex.cp.flexiexpensesmanager.features.tag.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.model.Tag
import com.charmflex.cp.flexiexpensesmanager.ui_common.AnnouncementPanel
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicColumnContainerItemList
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.BlurredBackgroundBox
import com.charmflex.cp.flexiexpensesmanager.ui_common.EmptyState
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultAnimation
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGActionDialog
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGLottieAnimation
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.features.SettingEditorScreen
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.rememberAnnouncementState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TagSettingScreen(
    viewModel: TagSettingViewModel
) {

    val viewState by viewModel.viewState.collectAsState()
    val announcementState = rememberAnnouncementState(viewState.announcement)

    BlurredBackgroundBox(
        blur = announcementState.isShowing()
    ) {
        TagListScreen(viewModel, viewState.tags, viewState.dialogState)

        SGAnimatedTransition(
            isVisible = viewState.isEditorMode,
            enter = slideInHorizontally(initialOffsetX = { it }),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            SettingEditorScreen(
                fields = viewState.tagEditorState?.fields ?: emptyList(),
                appBarTitle = stringResource(Res.string.tag_setting_editor_app_bar_title_add_new),
                screenName = "TagSettingScreen",
                onTextFieldChanged = { newValue, field ->
                    viewModel.onUpdateFields(
                        field,
                        newValue
                    )
                },
                onBack = viewModel::onBack
            ) {
                viewModel.addNewTag()
            }
        }
    }

    AnnouncementPanel(
        announcementState,
        onClosed = { viewModel.hideAnnouncement() }
    ) {

    }
}

@Composable
private fun TagListScreen(
    viewModel: TagSettingViewModel,
    tags: List<Tag>,
    dialogState: TagSettingViewState.TagSettingDialogState?
) {

    SGScaffold(
        modifier = Modifier
            .fillMaxWidth()
            .padding(grid_x2),
        topBar = {
            BasicTopBar(title = stringResource(Res.string.tag_setting_app_bar_title),
                actions = {
                    IconButton(onClick =  { viewModel.onToggleEditor(null) }) {
                        SGIcons.Add()
                    }
                }
            )
        }
    ) {
        if (tags.isEmpty()) {
            NoResultContent(description = "We didn't find anything. Create one?")
            return@SGScaffold
        }

        BasicColumnContainerItemList(
            items = tags,
            itemContent = {
                FEBody2(modifier = Modifier.weight(1f), text = "#${it.name}")
            },
            actionItems = {
                IconButton(onClick = { viewModel.onDeleteIconTap(it) }) {
                    SGIcons.Delete()
                }
            }
        )
    }

    dialogState?.let {
        when (it) {
            is TagSettingViewState.TagSettingDialogState.DeleteWarning -> {
                SGActionDialog(
                    title = stringResource(Res.string.generic_warning),
                    text = stringResource(Res.string.generic_delete_tag_warning_subtitle, it.tag.name),
                    onDismissRequest = { viewModel.onCloseDialog() },
                    primaryButtonText = stringResource(Res.string.generic_delete)
                ) {
                    viewModel.onConfirmDelete(tag = it.tag)
                }
            }
            else -> {}
        }
    }
}