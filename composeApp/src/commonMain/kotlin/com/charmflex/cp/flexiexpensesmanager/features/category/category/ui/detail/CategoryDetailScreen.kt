package com.charmflex.cp.flexiexpensesmanager.features.category.category.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charmflex.cp.flexiexpensesmanager.features.transactions.ui.transaction_history.TransactionHistoryList
import com.charmflex.cp.flexiexpensesmanager.ui_common.BasicTopBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.DateFilterBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGScaffold
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.LayeredComponent
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.TextComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.fill
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.patrykandpatrick.vico.multiplatform.common.shape.MarkerCorneredShape

@Composable
internal fun CategoryDetailScreen(
    viewModel: CategoryDetailViewModel
) {
    val viewState by viewModel.categoryDetailViewState.collectAsState()
    val dateFilter by viewModel.dateFilter.collectAsState()

    val RangeProvider = CartesianLayerRangeProvider.fixed(maxY = 100.0)
    val StartAxisValueFormatter = CartesianValueFormatter.decimal(suffix = "%")
    val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(suffix = "%")

    SGScaffold(
        modifier = Modifier
            .fillMaxSize().padding(grid_x2),
        topBar = {
            BasicTopBar(
                title = viewState.categoryName
            )
        },
        screenName = "CategoryDetailScreen"
    ) {
//        val x = viewState.lineChartData.entries.map { it.first }
//        val y = viewState.lineChartData.entries.map { it.second }
//        val modelProducer = remember { CartesianChartModelProducer() }
//        LaunchedEffect(Unit) {
//            modelProducer.runTransaction {
//                // Learn more: https://patrykandpatrick.com/z5ah6v.
//                lineSeries { series(x, y) }
//            }
//        }
//        val lineColor = Color(0xffa485e0)
//        CartesianChartHost(
//            rememberCartesianChart(
//                rememberLineCartesianLayer(
//                    lineProvider =
//                    LineCartesianLayer.LineProvider.series(
//                        LineCartesianLayer.rememberLine(
//                            fill = LineCartesianLayer.LineFill.single(fill(lineColor)),
//                            areaFill =
//                            LineCartesianLayer.AreaFill.single(
//                                fill(
//                                    Brush.verticalGradient(listOf(lineColor.copy(alpha = 0.4f), Color.Transparent))
//                                )
//                            ),
//                        )
//                    ),
//                    rangeProvider = RangeProvider,
//                ),
//                startAxis = VerticalAxis.rememberStart(valueFormatter = StartAxisValueFormatter),
//                bottomAxis = HorizontalAxis.rememberBottom(),
//                marker = rememberMarker(MarkerValueFormatter),
//            ),
//            modelProducer,
//            Modifier.height(216.dp),
//            rememberVicoScrollState(scrollEnabled = false),
//        )
        DateFilterBar(
            currentDateFilter = dateFilter,
            onDateFilterChanged = { viewModel.onDateChanged(it) }
        )
        TransactionHistoryList(
            transactionHistoryViewModel = viewModel
        )
    }
}

@Composable
internal fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter =
        DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = MarkerCorneredShape(CorneredShape.Corner.Rounded)
    val labelBackground =
        rememberShapeComponent(
            fill = fill(MaterialTheme.colorScheme.background),
            shape = labelBackgroundShape,
            strokeFill = fill(MaterialTheme.colorScheme.outline),
            strokeThickness = 1.dp,
        )
    val label =
        rememberTextComponent(
            style =
            TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
            ),
            padding = Insets(8.dp, 4.dp),
            background = labelBackground,
            minWidth = TextComponent.MinWidth.fixed(40.dp),
        )
    val indicatorFrontComponent =
        rememberShapeComponent(fill(MaterialTheme.colorScheme.surface), CorneredShape.Pill)
    val guideline = rememberAxisGuidelineComponent()
    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator =
        if (showIndicator) {
            { color ->
                LayeredComponent(
                    back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CorneredShape.Pill),
                    front =
                    LayeredComponent(
                        back = ShapeComponent(fill = Fill(color), shape = CorneredShape.Pill),
                        front = indicatorFrontComponent,
                        padding = Insets(5.dp),
                    ),
                    padding = Insets(10.dp),
                )
            }
        } else {
            null
        },
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}