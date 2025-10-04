package com.charmflex.cp.flexiexpensesmanager.features.announcement.destination

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.navigation.DestinationBuilder
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AnnouncementRoute
import com.charmflex.cp.flexiexpensesmanager.core.utils.getViewModel
import com.charmflex.cp.flexiexpensesmanager.features.announcement.ui.AnnouncementScreen
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RemoteConfigScene
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGAnimatedTransition
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_referral
import org.jetbrains.compose.resources.stringResource

internal class AnnouncementDestinationBuilder(
) : DestinationBuilder {
    private val appComponent = AppComponentProvider.instance.getAppComponent()
    override fun NavGraphBuilder.buildGraph() {
        root()
    }

    private fun NavGraphBuilder.root() {
        composable<AnnouncementRoute.Root> {
            val route = it.toRoute<AnnouncementRoute.Root>()
            val viewModel = getViewModel {
                appComponent.announcementViewModelFactory().create(route.remoteConfigScene)
            }
            val viewState by viewModel.viewState.collectAsState()

            val appBarTitle = when (route.remoteConfigScene) {
                RemoteConfigScene.REFERRAL -> stringResource(Res.string.generic_referral)
                else -> ""
            }

            AnnouncementScreen(
                appBarTitle = appBarTitle,
                isLoading = viewState.isLoading,
                announcement = viewState.announcement,
                onNotShowAgainChecked = {
                    viewState.announcement?.let {
                        viewModel.doNotShowAgainTriggered(
                            route.remoteConfigScene, it.version
                        )
                    }

                },
                onClosed = { viewModel.onClosed() }
            ) {
                viewModel.onAction(it)
            }
        }
    }


}