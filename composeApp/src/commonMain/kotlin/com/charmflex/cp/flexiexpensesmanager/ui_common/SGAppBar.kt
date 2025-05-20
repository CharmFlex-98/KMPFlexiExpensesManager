package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.charmflex.cp.flexiexpensesmanager.ui_common.theme.FlexiExpensesManagerTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTopBar(
    title: String = "",
    leadingIcon: (@Composable () -> Unit)? = null,
    leadingIconAction: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (leadingIconAction != null && leadingIcon != null) {
                IconButton(onClick = leadingIconAction, content = leadingIcon)
            }
        },
        actions = actions
    )
}

@Composable
@Preview
fun BasicTopBarPreview() {
    FlexiExpensesManagerTheme {
        BasicTopBar(title = "Testing")
    }
}