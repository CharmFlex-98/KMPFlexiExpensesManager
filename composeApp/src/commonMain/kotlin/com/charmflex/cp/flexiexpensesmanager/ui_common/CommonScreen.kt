package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}