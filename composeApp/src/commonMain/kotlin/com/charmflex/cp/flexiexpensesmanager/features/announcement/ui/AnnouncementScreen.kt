package com.charmflex.cp.flexiexpensesmanager.features.announcement.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.ActionType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.ui_common.AnnouncementPanel
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.rememberAnnouncementState


@Composable
internal fun AnnouncementScreen(
    appBarTitle: String,
    isLoading: Boolean,
    announcement: RCAnnouncementResponse? = null,
    onNotShowAgainChecked: (() -> Unit)? = null,
    onClosed: () -> Unit,
    onAction: (ActionType) -> Unit,
) {
    val state = rememberAnnouncementState(announcement)
    SGScaffold(
        modifier = Modifier.fillMaxSize().padding(grid_x2),
        isLoading = isLoading,
        screenName = "AnnouncementScreen",
        topBar = {
            BasicTopBar(title = appBarTitle)
        }
    ) {
        announcement?.let {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AnnouncementPanel(
                    state,
                    onNotShowAgainChecked = onNotShowAgainChecked,
                    onClosed = onClosed
                ) {
                    onAction(it)
                }
            }
        }
    }
}