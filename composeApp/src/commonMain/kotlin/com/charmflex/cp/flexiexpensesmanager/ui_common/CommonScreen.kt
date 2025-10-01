package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.ActionType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.IconType
import com.charmflex.cp.flexiexpensesmanager.features.remote.remote_config.models.RCAnnouncementResponse
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.generic_import_data
import kotlinproject.composeapp.generated.resources.generic_ok
import kotlinproject.composeapp.generated.resources.lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ColumnScope.LockedScreen(
    isFeatureUnlocked: Boolean,
    unlockedTitle: String,
    unlockedSubtitle: String,
    unlockedDrawableRes: DrawableResource
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(grid_x6)
                .background(
                    color = if (isFeatureUnlocked) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(grid_x1_5)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(
                    if (isFeatureUnlocked) unlockedDrawableRes
                    else Res.drawable.lock
                ),
                contentDescription = null,
                tint = if (isFeatureUnlocked) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(grid_x3)
            )
        }

        Spacer(modifier = Modifier.height(grid_x2))

        Text(
            text = if (isFeatureUnlocked) {
                unlockedTitle
            } else {
                "Import Feature Locked"
            },
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(grid_x1))

        Text(
            text = if (isFeatureUnlocked) {
                unlockedSubtitle
            } else {
                "Unlock this premium feature to restore your transaction data from backup files"
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
internal fun AnnouncementPanel(
    announcementState: AnnouncementState,
    onClosed: () -> Unit,
    onAction: (ActionType) -> Unit,
) {
    val announcement = announcementState.value ?: return
    val onClosedAction = {
        announcementState.toggleShow(false)
        onClosed()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SGAnimatedTransition(
            isVisible = announcementState.isShowing(),
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + expandIn(
                expandFrom = Alignment.Center,
                animationSpec = tween(durationMillis = 500)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)) +
                    shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = tween(durationMillis = 500)
                    )
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(maxWidth * 0.85f)
                        .height(maxHeight * 0.55f),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(grid_x2)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = announcement.label,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            if (announcement.closable) {
                                IconButton(onClick = onClosedAction) {
                                    SGIcons.Close()
                                }
                            }
                        }


                        FEHeading3(
                            modifier = Modifier.fillMaxWidth().padding(vertical = grid_x1),
                            textAlign = TextAlign.Center,
                            text = announcement.title,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Animation section with background
                        when (announcement.iconType) {
                            IconType.ANNOUNCEMENT -> AnnouncementAnimation()
                            else -> AnnouncementAnimation()
                        }

                        // Content section
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FEBody1(
                                text = announcement.subtitle,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Spacer(modifier = Modifier.weight(1f))
                            SGSmallPrimaryButton(
                                text = stringResource(Res.string.generic_ok),
                            ) {
                                if (announcement.actionType == ActionType.CLOSE) {
                                    onClosedAction()
                                }
                                onAction(announcement.actionType)
                            }
                        }
                    }
                }
            }
        }
    }
}

internal class AnnouncementState(
    announcementResponse: RCAnnouncementResponse? = null
) {
    var value by mutableStateOf(announcementResponse)
        private set

    fun toggleShow(show: Boolean) {
        value = value?.copy(show = show )
    }

    fun isShowing(): Boolean {
        return value?.show == true
    }
}

@Composable
internal fun rememberAnnouncementState(value: RCAnnouncementResponse?): AnnouncementState {
    return remember(value) { AnnouncementState(value) }
}