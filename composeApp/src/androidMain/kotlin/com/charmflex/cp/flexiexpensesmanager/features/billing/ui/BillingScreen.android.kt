package com.charmflex.cp.flexiexpensesmanager.features.billing.ui

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.CompactProductCard
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGSnackBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarType
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.showSnackBarImmediately
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.billing_setting_app_bar_title
import kotlinproject.composeapp.generated.resources.budget_setting_app_bar_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal actual fun BillingScreen(viewModel: BillingViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    val viewState by viewModel.viewState.collectAsState()
    val snackbarState by viewModel.snackBarState
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberLazyListState()

    LaunchedEffect(snackbarState) {
        if (snackbarState.isNotBlank()) {
            snackbarHostState.showSnackBarImmediately(snackbarState)
            viewModel.resetState()
        }
    }

    if (activity == null) {
        NoResultContent(modifier = Modifier.fillMaxSize(), "Something went wrong. Please retry later")
        return
    }


    SGScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(grid_x2),
        topBar = {
            BasicTopBar(
                title = stringResource(Res.string.billing_setting_app_bar_title),
            )
        },
        screenName = "BillingScreen"
    ) {
        if (viewState.productInfos.isEmpty()) {
            NoResultContent(
                modifier = Modifier.fillMaxSize(),
                "No available in-app purchase items."
            )
            return@SGScaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = grid_x2),
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(grid_x2)
        ) {
            itemsIndexed(viewState.productInfos) { index, product ->
                CompactProductCard (
                    product = product,
                    onClick = { viewModel.purchaseProduct(it.productId) }
                )
            }
        }
    }
    SGSnackBar(snackBarHostState = snackbarHostState, snackBarType = SnackBarType.Error)
}