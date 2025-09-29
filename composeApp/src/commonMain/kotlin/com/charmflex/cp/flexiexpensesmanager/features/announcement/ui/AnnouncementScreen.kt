package com.charmflex.cp.flexiexpensesmanager.features.announcement.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.IconType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import com.charmflex.cp.flexiexpensesmanager.ui_common.AnnouncementPanel
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2


@Composable
internal fun AnnouncementScreen(
    appBarTitle: String,
    isLoading: Boolean,
    onClosed: () -> Unit,
    announcement: RCAnnouncementResponse? = null,
    onAction: () -> Unit,
) {
    SGScaffold(
        modifier = Modifier.fillMaxSize().padding(grid_x2),
        isLoading = isLoading,
        screenName = "AnnouncementScreen",
        topBar = {
            BasicTopBar(title = appBarTitle)
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AnnouncementPanel(
                show = announcement != null,
                chipText = announcement?.label ?: "",
                iconType = announcement?.iconType ?: IconType.ANNOUNCEMENT,
                title = announcement?.title ?: "",
                subtitle = announcement?.subtitle ?: "",
                closable = announcement?.closable ?: true,
                onClosed = onClosed
            ) {
                onAction()
            }
        }
    }
}