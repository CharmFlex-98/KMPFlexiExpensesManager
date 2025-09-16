package com.charmflex.cp.flexiexpensesmanager.features.billing.ui

import ProductInfo
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.CompactProductCard
import com.charmflex.cp.flexiexpensesmanager.ui_common.NoResultContent
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.billing_setting_app_bar_title
import org.jetbrains.compose.resources.stringResource

enum class BillingLayoutType {
    COMPACT
}


@Composable
internal actual fun BillingScreenWithOptions(viewModel: BillingViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity

    val viewState by viewModel.viewState.collectAsState()
    var layoutType by remember { mutableStateOf(BillingLayoutType.COMPACT) }

    if (activity == null) {
        return
    }

    val billingManager = remember {
        AndroidBillingManager(activity)
    }

    LaunchedEffect(billingManager) {
        viewModel.init(billingManager)
    }

    DisposableEffect(billingManager) {
        onDispose {
            billingManager.cleanup()
        }
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
        isLoading = false
    ) {
        if (viewState.productInfos.isEmpty()) {
            NoResultContent(
                modifier = Modifier.fillMaxSize(),
                "No available in-app purchase items."
            )
            return@SGScaffold
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Layout Selection Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = grid_x2)
            ) {
                Text(
                    text = "Choose Your Plan",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "Unlock premium features and enhance your experience",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Layout Type Selector
                LayoutTypeSelector(
                    currentLayout = layoutType,
                    onLayoutChange = { layoutType = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on selected layout
            when (layoutType) {
                BillingLayoutType.COMPACT -> {
                    CompactLayout(
                        products = viewState.productInfos,
                        onProductClick = { viewModel.purchaseProduct(it.productId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LayoutTypeSelector(
    currentLayout: BillingLayoutType,
    onLayoutChange: (BillingLayoutType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = currentLayout == BillingLayoutType.COMPACT,
            onClick = { onLayoutChange(BillingLayoutType.COMPACT) },
            label = { Text("Compact") }
        )
    }
}


@Composable
private fun CompactLayout(
    products: List<ProductInfo>,
    onProductClick: (ProductInfo) -> Unit
) {
    val scrollState = rememberLazyListState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = grid_x2),
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(products) { index, product ->
            CompactProductCard(
                product = product,
                onClick = onProductClick
            )
        }
    }
}
