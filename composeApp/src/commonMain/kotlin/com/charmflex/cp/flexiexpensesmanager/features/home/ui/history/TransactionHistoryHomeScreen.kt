package com.charmflex.cp.flexiexpensesmanager.features.home.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.TransactionHistoryList
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultAnimation
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2

@Composable
internal fun TransactionHistoryHomeScreen(
    transactionHomeViewModel: TransactionHomeViewModel
) {
    val viewState by transactionHomeViewModel.viewState.collectAsState()
    if (viewState.items.isEmpty()) {
        NoResultContent(modifier = Modifier.fillMaxSize(), "No available history found.")
        return
    }

    TransactionHistoryList(
        transactionHistoryViewModel = transactionHomeViewModel
    )
    if (viewState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}