package com.charmflex.cp.flexiexpensesmanager.features.announcement.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.storage.SharedPrefs
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.ToastManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.announcement.service.AnnouncementService
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.ActionType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementRequest
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.repository.RemoteConfigRepository
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.RedirectPath
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.Redirector
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_error
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

const val ANNOUNCEMENT_DO_NOT_SHOW_AGAIN_PREFIX = "announcement_do_not_show_again_"

@Factory
internal class AnnouncementViewModelFactory(
    private val announcementService: AnnouncementService,
    private val toastManager: ToastManager,
    private val resourcesProvider: ResourcesProvider,
    private val routeNavigator: RouteNavigator,
    private val redirector: Redirector
) {
    fun create(scene: RemoteConfigScene): AnnouncementViewModel {
        return AnnouncementViewModel(
            scene,
            announcementService,
            toastManager,
            resourcesProvider,
            routeNavigator,
            redirector
        )
    }
}


internal class AnnouncementViewModel(
    private val scene: RemoteConfigScene,
    private val announcementService: AnnouncementService,
    private val toastManager: ToastManager,
    private val resourcesProvider: ResourcesProvider,
    private val routeNavigator: RouteNavigator,
    private val redirector: Redirector
) : ViewModel() {


    private val _viewState = MutableStateFlow(AnnouncementViewState())
    val viewState = _viewState.asStateFlow()

    init {
        _viewState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            resultOf {
                announcementService.getSceneAnnouncement(RCAnnouncementRequest(scene))
            }.onSuccess { res ->
                _viewState.update {
                    it.copy(
                        isLoading = false,
                        announcement = res
                    )
                }
            }.onFailure {
                toastManager.postError(resourcesProvider.getString(Res.string.generic_error))
                _viewState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun doNotShowAgainTriggered(scene: RemoteConfigScene, version: Int) {
        announcementService.doNotShowAgainScene(scene, version)
    }

    fun onClosed() {
        closeAnnouncement()
    }

    fun onAction(actionType: ActionType) {
        when (actionType) {
            ActionType.BACK -> routeNavigator.pop()
            ActionType.UPDATE_AT_STORE -> {
                redirector.redirectTo(RedirectPath.OFFICIAL_STORE)
            }
            else -> {}
        }
    }

    private fun closeAnnouncement() {
        _viewState.update {
            it.copy(
                announcement = it.announcement?.copy(show = false)
            )
        }
    }
}



internal data class AnnouncementViewState(
    val isLoading: Boolean = false,
    val announcement: RCAnnouncementResponse? = null
)