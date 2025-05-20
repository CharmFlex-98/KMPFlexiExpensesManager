package com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_pie_chart

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import com.charmflex.cp.flexiexpensesmanager.ui_common.DateFilterBar
import com.charmflex.cp.flexiexpensesmanager.ui_common.FECallout3
import com.charmflex.cp.flexiexpensesmanager.ui_common.FEHeading4
import com.charmflex.cp.flexiexpensesmanager.ui_common.SGMediumPrimaryButton
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x1
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x2
import com.charmflex.cp.flexiexpensesmanager.ui_common.grid_x47
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random


@Composable
internal fun ColumnScope.ExpensesChartScreen(
    viewModel: ExpensesChartViewModel
) {
    val chartViewState by viewModel.viewState.collectAsState()
    val tagFilters by viewModel.tagFilter.collectAsState()
    val chartType = chartViewState.chartType
    val dateFilter by viewModel.dateFilter.collectAsState()
    Column(
        modifier = Modifier
            .padding(horizontal = grid_x1)
            .weight(1f)
    ) {
        DateFilterBar(
            currentDateFilter = dateFilter,
            onDateFilterChanged = {
                viewModel.onDateFilterChanged(it)
            }
        )
        Row(modifier = Modifier.padding(vertical = grid_x1)) {
            TextButton(
                onClick = { viewModel.onNavigateBudgetDetail() },
                border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                FECallout3(text = stringResource(Res.string.budget_detail_home_button))
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { viewModel.onNavigateExpensesDetailPage() },
                border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.tertiary)
            ) {
                FECallout3(text = stringResource(Res.string.expenses_chart_detail_button))
            }
        }

        TabRow(selectedTabIndex = chartType.index) {
            getTabs().forEachIndexed { index, chartType ->
                Tab(
                    selected = index == chartType.index,
                    onClick = { viewModel.toggleChartType(chartType) }) {
                    FEHeading4(text = chartType.name)
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = grid_x1),
            contentAlignment = Alignment.Center
        ) {
            when (chartType) {
                is ExpensesPieChartViewState.ChartType.Pie -> {
                    PieChart(
                        modifier = Modifier.fillMaxSize(),
                        animation = TweenSpec(durationMillis = 1000),
                        pieChartData = chartViewState.pieChartData.map {
                            PieChartData(
                                it.data,
                                it.color,
                                it.partName
                            )
                        },
                        ratioLineColor = Color.LightGray,
                        textRatioStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        descriptionStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                        legendPosition = LegendPosition.BOTTOM
                    )
                }

                is ExpensesPieChartViewState.ChartType.Bar -> {
                    ComposeChart6(chartViewState)
                }
            }
        }
    }


    if (chartViewState.showTagFilterDialog) {
        var tagFilterTemp by remember {
            mutableStateOf(tagFilters)
        }

        Dialog(onDismissRequest = { viewModel.onToggleTagDialog(false) }) {
            Column(
                modifier = Modifier
                    .height(grid_x47)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .verticalScroll(rememberScrollState())
            ) {
                tagFilterTemp.forEachIndexed { index, it ->
                    if (index != 0) HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    Row(
                        modifier = Modifier.padding(grid_x1),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = it.name,
                            textAlign = TextAlign.Start
                        )
                        RadioButton(selected = it.selected, onClick = {
                            tagFilterTemp = tagFilterTemp.map { item ->
                                if (it.id == item.id) {
                                    it.copy(
                                        selected = !it.selected
                                    )
                                } else item
                            }
                        })
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(grid_x2),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    SGMediumPrimaryButton(modifier = Modifier.fillMaxWidth(), text = "Set") {
                        viewModel.onSetTagFilter(tagFilterTemp)
                    }
                }
            }
        }
    }
}

private fun getTabs(): List<ExpensesPieChartViewState.ChartType> {
    return listOf(
        ExpensesPieChartViewState.ChartType.Pie(),
        ExpensesPieChartViewState.ChartType.Bar()
    )
}

private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
private val columnColors = listOf(Color(0xff3e6558), Color(0xff5e836a), Color(0xffa5ba8e))

@Composable
private fun ComposeChart6(
    viewState: ExpensesPieChartViewState,
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/3aqy4o.
            columnSeries {
                val yValues = viewState.barChartData.categoryExpensesAmount.map { it.second }
                series(yValues)
            }
        }
    }
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer = modelProducer,
        modifier = Modifier,
    )
}

internal sealed class FilterMenuDropDownItem(
    val titleResId: StringResource,
    val filterMenuItemType: FilterMenuItemType
) {

    object All : FilterMenuDropDownItem(
        titleResId = Res.string.date_filter_all,
        filterMenuItemType = FilterMenuItemType.ALL
    )

    object Monthly : FilterMenuDropDownItem(
        titleResId = Res.string.date_filter_monthly,
        filterMenuItemType = FilterMenuItemType.MONTHLY
    )

    object Custom : FilterMenuDropDownItem(
        titleResId = Res.string.date_filter_custom,
        filterMenuItemType = FilterMenuItemType.CUSTOM
    )

    enum class FilterMenuItemType {
        MONTHLY, CUSTOM, ALL
    }
}

private const val COLOR_1_CODE = 0xff3e6558
private const val COLOR_2_CODE = 0xff5e836a
private const val COLOR_3_CODE = 0xffa5ba8e
private const val COLOR_4_CODE = 0xffe9e5af
private const val THRESHOLD_LINE_VALUE_RANGE_START = 7f
private const val THRESHOLD_LINE_VALUE_RANGE_END = 14f
private const val THRESHOLD_LINE_ALPHA = .36f
private const val COLUMN_CORNER_CUT_SIZE_PERCENT = 50

private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val color3 = Color(COLOR_3_CODE)
private val color4 = Color(COLOR_4_CODE)
private val chartColors = listOf(color1, color2, color3)
private val thresholdLineValueRange =
    THRESHOLD_LINE_VALUE_RANGE_START..THRESHOLD_LINE_VALUE_RANGE_END
private val thresholdLineLabelHorizontalPaddingValue = 8.dp
private val thresholdLineLabelVerticalPaddingValue = 2.dp
private val thresholdLineLabelMarginValue = 4.dp
private val thresholdLineColor = color4.copy(THRESHOLD_LINE_ALPHA)


private const val LABEL_BACKGROUND_SHADOW_RADIUS_DP = 4f
private const val LABEL_BACKGROUND_SHADOW_DY_DP = 2f
private const val CLIPPING_FREE_SHADOW_RADIUS_MULTIPLIER = 1.4f


fun generateRandomColor(): Color {
    val random = Random.Default
    val red = random.nextInt(256)
    val green = random.nextInt(256)
    val blue = random.nextInt(256)
    return Color(red, green, blue)
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val LABEL_LINE_COUNT = 1
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val GUIDELINE_DASH_LENGTH_DP = 8f
private const val GUIDELINE_GAP_LENGTH_DP = 4f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelHorizontalPaddingValue = 8.dp
private val labelVerticalPaddingValue = 4.dp
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
private val guidelineThickness = 2.dp