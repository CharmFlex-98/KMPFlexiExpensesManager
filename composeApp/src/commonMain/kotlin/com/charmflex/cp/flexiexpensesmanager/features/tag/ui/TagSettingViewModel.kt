package com.charmflex.cp.flexiexpensesmanager.features.tag.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.domain.FEField
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.BackupRoutes
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.announcement.service.AnnouncementService
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.ActionType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.repository.RemoteConfigRepository
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.model.Tag
import com.charmflex.cp.flexiexpensesmanager.features.tag.domain.repositories.TagRepository
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.RedirectPath
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.Redirector
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import com.charmflex.cp.flexiexpensesmanager.ui_common.features.SETTING_EDITOR_TAG
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.koin.core.annotation.Factory

@Factory
internal class TagSettingViewModel constructor(
    private val tagRepository: TagRepository,
    private val routeNavigator: RouteNavigator,
    private val resourcesProvider: ResourcesProvider,
    private val announcementService: AnnouncementService,
    private val toastManager: ToastManager,
    private val redirector: Redirector
) : ViewModel() {
    private var flowType: TagSettingFlow = TagSettingFlow.Default

    private val _viewState = MutableStateFlow(TagSettingViewState())
    val viewState = _viewState.asStateFlow()

    private val _snackBarState: MutableStateFlow<SnackBarState> = MutableStateFlow(SnackBarState.None)
    val snackBarState = _snackBarState.asStateFlow()

    private val _bsState = MutableStateFlow<TagSettingViewState.TagSettingDialogState?>(null)
    val bsState = _bsState.asStateFlow()

    init {
        observeTags()
        observeAnnouncement()
    }

    private fun observeAnnouncement() {
        viewModelScope.launch {
            resultOf {
                announcementService.getSceneAnnouncement(RCAnnouncementRequest(RemoteConfigScene.TAG))
            }.onSuccess { res ->
                _viewState.update {
                    it.copy(
                        announcement = res
                    )
                }
            }.onFailure {
                toastManager.postError(resourcesProvider.getString(Res.string.toast_connection_lost_unable_fetch_config))
            }
        }
    }

    fun doNotShowAgain() {
        _viewState.value.announcement?.version?.let {
            announcementService.doNotShowAgainScene(RemoteConfigScene.TAG, it)
        }
    }

    fun hideAnnouncement() {
        _viewState.update {
            it.copy(
                announcement = _viewState.value.announcement?.copy(show = false)
            )
        }
    }

    fun initFlow(fixImportTagName: String?) {
        if (fixImportTagName != null) flowType =
            TagSettingFlow.ImportFix(fixImportTagName) else TagSettingFlow.Default
        when (val ft = flowType) {
            is TagSettingFlow.ImportFix -> {
                onToggleEditor(null)
                getTagField()?.let { f ->
                    onUpdateFields(feField = f, ft.tagName)
                }

            }
            else -> {}
        }
    }

    fun onUpdateFields(feField: FEField, newValue: String, newId: String? = null) {
        val tagEditorState = _viewState.value.tagEditorState
        tagEditorState?.let {
            val updatedFields = it.fields.map { item ->
                if (item == feField) return@map item.copy(valueItem = item.valueItem.copy(id = newId ?: "", value = newValue))
                item
            }
            _viewState.update { vs ->
                vs.copy(
                    tagEditorState = it.copy(fields = updatedFields)
                )
            }
        }
    }

    fun onAnnouncementAction(actionType: ActionType) {
        when (actionType) {
            ActionType.UPDATE_AT_STORE -> {
                redirector.redirectTo(RedirectPath.OFFICIAL_STORE)
            }
            else -> {}
        }
    }

    private fun observeTags() {
        viewModelScope.launch {
            tagRepository.getAllTags().collectLatest { res ->
                _viewState.update {
                    it.copy(
                        tags = res
                    )
                }
            }
        }
    }

    fun addNewTag() {
        val tagName = getTagField()?.valueItem?.value
        if (tagName.isNullOrEmpty()) {
            handleError(TagErrorState.BlankTag)
            return
        }

        viewModelScope.launch {
            resultOf {
                tagRepository.addTag(tagName)
            }.fold(
                onSuccess = {
                    onBack()
                },
                onFailure = {}
            )
        }
    }

    private fun getTagField(): FEField? {
        return _viewState.value.tagEditorState?.fields?.firstOrNull { it.id == SETTING_EDITOR_TAG }
    }

    fun onBack() {
        when (flowType) {
            is TagSettingFlow.Default -> {
                if (viewState.value.isEditorMode) _viewState.value = _viewState.value.copy(tagEditorState = null)
                else routeNavigator.pop()
            }
            is TagSettingFlow.ImportFix -> {
                routeNavigator.popWithArguments(
                    mapOf(
                        BackupRoutes.Args.UPDATE_IMPORT_DATA to true
                    )
                )
            }
        }
    }

    fun onDeleteIconTap(tag: Tag) {
        _viewState.update {
            it.copy(
                dialogState = TagSettingViewState.TagSettingDialogState.DeleteWarning(tag)
            )
        }
    }

    fun onConfirmDelete(tag: Tag) {
        viewModelScope.launch {
            tagRepository.deleteTag(tagId = tag.id)
            _viewState.update {
                it.copy(
                    dialogState = null
                )
            }
        }
    }

    fun onCloseDialog() {
        _viewState.update {
            it.copy(
                dialogState = null
            )
        }
    }

    fun onToggleEditor(selectedTag: Tag?) {
        _viewState.update {
            it.copy(
                tagEditorState = TagSettingViewState.TagEditorState(tagForEdit = selectedTag)
            )
        }
    }

    private fun handleError(errorState: TagErrorState) {
        _snackBarState.value = SnackBarState.Error(message = resourcesProvider.getString(errorState.errorId))
    }
}


internal data class TagSettingViewState(
    val selectedTag: Tag? = null,
    val tags: List<Tag> = emptyList(),
    val tagEditorState: TagEditorState? = null,
    val dialogState: TagSettingDialogState? = null,
    val announcement: RCAnnouncementResponse? = null
) {
    val isEditorMode: Boolean get() = tagEditorState != null

    internal data class TagEditorState(
        val fields: List<FEField> = tagFieldsProvider(),
        val tagForEdit: Tag?
    )

    internal sealed interface TagSettingDialogState {
        data object None: TagSettingDialogState
        data class DeleteWarning(
            val tag: Tag,
        ) : TagSettingDialogState
    }
}

private sealed class TagErrorState(
    val errorId: StringResource
) {
    data object BlankTag: TagErrorState(Res.string.generic_blank_input_invalid)
}

internal sealed interface TagSettingFlow {
    object Default : TagSettingFlow
    data class ImportFix(
        val tagName: String
    ) : TagSettingFlow
}

private fun tagFieldsProvider(): List<FEField> {
    return listOf(
        FEField(
            id = SETTING_EDITOR_TAG,
            labelId = Res.string.tag_setting_tag_name,
            hintId = Res.string.tag_setting_tag_name_hint,
            type = FEField.FieldType.Text,
        )
    )
}