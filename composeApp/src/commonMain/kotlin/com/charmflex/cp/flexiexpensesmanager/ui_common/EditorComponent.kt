package com.charmflex.cp.flexiexpensesmanager.ui_common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.AccountEditorViewState
import com.charmflex.cp.flexiexpensesmanager.features.account.ui.TapFieldType
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.account_editor_amount_label
import kotlinproject.composeapp.generated.resources.account_editor_currency_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> SelectionItem(
    item: T,
    title: (T) -> String,
    subtitle: ((T) -> String)? = null,
    onClick: ((T) -> Unit)? = null,
    onSubIconClick: ((T) -> Unit)? = null,
    suffixIcon: (@Composable (T) -> Unit)? = null,
    subPrefixIcon: (@Composable (T) -> Unit)? = null,
    showDivider: Boolean = true,
) {
    Column {
        if (showDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }

        Surface(
            modifier = Modifier.fillMaxWidth().padding(grid_x1),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title(item),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    subtitle?.let {
                        Text(
                            text = subtitle(item),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (onSubIconClick != null) {
                    IconButton(
                        modifier = Modifier.size(grid_x3),
                        onClick = { onSubIconClick(item) },
                        content = { subPrefixIcon?.let{ it(item) } }
                    )
                }

                Spacer(modifier = Modifier.width(grid_x4))

                if (onClick != null) {
                    IconButton(
                        modifier = Modifier.size(grid_x3),
                        onClick = { onClick(item) },
                        content = { suffixIcon?.let{ it(item) } }
                    )
                }
            }
        }
    }
}


@Composable
fun EditorCard(
    modifier: Modifier = Modifier,
    header: String,
    buttonText: String,
    onButtonClicked: () -> Unit,
    cardContents: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = header,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                cardContents()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action Button
        SGLargePrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = buttonText,
        ) {
            onButtonClicked()
        }
    }
}