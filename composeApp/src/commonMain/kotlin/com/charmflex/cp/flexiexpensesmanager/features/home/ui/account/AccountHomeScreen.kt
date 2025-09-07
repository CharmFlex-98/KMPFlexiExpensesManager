package com.charmflex.cp.flexiexpensesmanager.features.home.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.ui_common.DateFilterBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody1
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEBody2
import com.charmflex.cp.flexiexpensesmanager.ui_common.FECallout3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading5
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEMetaData1
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGIcons
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x0_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2_5
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x3
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x4
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AccountHomeScreen(viewModel: AccountHomeViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val dateFilter by viewModel.dateFilter.collectAsState()
    val totalAsset = if (viewState.hideInfo) HIDE_INFO_LABEL else viewState.totalAsset

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = grid_x2, vertical = grid_x1)
    ) {
        // Date Filter Section
        DateFilterBar(
            currentDateFilter = dateFilter,
            onDateFilterChanged = { viewModel.onDateFilterChanged(it) },
        )

        Spacer(modifier = Modifier.height(grid_x2))

        // Total Assets Card
        TotalAssetsCard(
            totalAsset = totalAsset,
            hideInfo = viewState.hideInfo,
            onToggleHideInfo = { viewModel.toggleHideInfo() }
        )

        Spacer(modifier = Modifier.height(grid_x3))

        // Account Groups
        viewState.accountsSummary.forEach { accountGroup ->
            EnhancedAccountGroupSection(
                accountGroupSummary = accountGroup,
                hideInfo = viewState.hideInfo,
                onAccountClick = { account -> viewModel.onAccountClick(account) }
            )

            Spacer(modifier = Modifier.height(grid_x2))
        }
    }
}

@Composable
private fun TotalAssetsCard(
    totalAsset: String,
    hideInfo: Boolean,
    onToggleHideInfo: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(grid_x3)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    FEMetaData1(
                        text = stringResource(Res.string.generic_total_asset).uppercase(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(grid_x0_5))

                    FEHeading3(
                        text = totalAsset
                    )
                }

                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                ) {
                    IconButton(
                        onClick = onToggleHideInfo,
                        modifier = Modifier.padding(grid_x0_5)
                    ) {
                        if (hideInfo) SGIcons.VisibilityOff() else SGIcons.VisibilityOn()
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedAccountGroupSection(
    accountGroupSummary: AccountHomeViewState.AccountGroupSummaryUI,
    hideInfo: Boolean,
    onAccountClick: (AccountHomeViewState.AccountGroupSummaryUI.AccountSummaryUI) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(grid_x2)
        ) {
            // Account Group Header
            AccountGroupHeader(
                accountGroupSummary = accountGroupSummary,
                hideInfo = hideInfo
            )

            if (accountGroupSummary.accountsSummary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(grid_x1_5))

                // Account Items
                accountGroupSummary.accountsSummary.forEachIndexed { index, account ->
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = grid_x1),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }

                    AccountItem(
                        account = account,
                        hideInfo = hideInfo,
                        onAccountClick = { onAccountClick(account) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountGroupHeader(
    accountGroupSummary: AccountHomeViewState.AccountGroupSummaryUI,
    hideInfo: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FEHeading4(
            modifier = Modifier.weight(1f),
            text = accountGroupSummary.accountGroupName,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(grid_x2))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            FEHeading5(
                text = if (hideInfo) HIDE_INFO_LABEL else accountGroupSummary.balance,
                color = getBalanceColor(accountGroupSummary.balanceInCent, hideInfo)
            )

            // Optional: Add account count
            FEMetaData1(
                text = "${accountGroupSummary.accountsSummary.size} accounts",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun AccountItem(
    account: AccountHomeViewState.AccountGroupSummaryUI.AccountSummaryUI,
    hideInfo: Boolean,
    onAccountClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(grid_x1))
            .clickable { onAccountClick() }
            .padding(vertical = grid_x1_5, horizontal = grid_x1),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.weight(1f)
        ) {
            FEBody1(
                text = account.accountName,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.width(grid_x2))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            FEBody2(
                text = if (hideInfo) HIDE_INFO_LABEL else account.balance,
                color = if (hideInfo) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary
            )

            if (!account.isCurrencyPrimary && !hideInfo) {
                Spacer(modifier = Modifier.height(grid_x0_5))

                FECallout3(
                    text = account.mainCurrencyBalance,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun getBalanceColor(balanceInCent: Long, hideInfo: Boolean): Color {
    return when {
        hideInfo -> MaterialTheme.colorScheme.onSurface
        balanceInCent < 0 -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }
}

private const val HIDE_INFO_LABEL = "*****"