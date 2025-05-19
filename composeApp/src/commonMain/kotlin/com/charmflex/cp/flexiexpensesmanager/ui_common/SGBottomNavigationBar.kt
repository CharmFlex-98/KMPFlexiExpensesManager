package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SGBottomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = NavigationBarDefaults.containerColor,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    tonalElevation: Dp = NavigationBarDefaults.Elevation,
    items: List<SGBottomNavItem>,
    isSelected: (String) -> Boolean,
    onClick: (SGBottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = isSelected(item.route),
                onClick = {
                    onClick(item)
                },
                label = { Text(text = stringResource(item.titleId))},
                icon = {
                    Icon(painter = painterResource(item.iconId), contentDescription = "")
                }
            )
        }
    }
}

data class SGBottomNavItem(
    val index: Int,
    val titleId: StringResource,
    val iconId: DrawableResource,
    val route: String
)

