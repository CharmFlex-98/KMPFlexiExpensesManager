package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun <T> BasicColumnContainerItemList(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemContent: @Composable RowScope.(T) -> Unit,
    actionItems: @Composable RowScope.(T) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(grid_x1)
    ) {
        items.forEachIndexed { index, it ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = grid_x0_5, vertical = grid_x0_5)
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(grid_x2),
                        spotColor = Color.Black.copy(alpha = 0.4f)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        shape = RoundedCornerShape(grid_x2)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(grid_x2),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(grid_x2)
                ) {
                    itemContent(it)
                    actionItems(it)
                }
            }
        }
    }
}


@Composable
internal fun FeColumnContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(grid_x2))
            .background(backgroundColor)
            .padding(horizontal = grid_x2, vertical = grid_x1),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        content()
    }
}


