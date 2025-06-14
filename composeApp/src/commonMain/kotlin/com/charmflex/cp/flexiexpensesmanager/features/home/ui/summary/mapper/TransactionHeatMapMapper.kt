package com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.charmflex.flexiexpensesmanager.core.utils.Mapper
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.summary.expenses_heat_map.Level
import com.charmflex.cp.flexiexpensesmanager.features.home.usecases.DailyTransaction
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import kotlin.math.max
import kotlin.math.min

@Factory
internal class TransactionHeatMapMapperFactory constructor() {
    fun create(
        lowerBoundThreshold: Float,
        higherBoundThreshold: Float
    ): TransactionHeatMapMapper {
        return TransactionHeatMapMapper(lowerBoundThreshold, higherBoundThreshold)
    }
}

internal class TransactionHeatMapMapper(
    private val lowerBoundThreshold: Float,
    private val higherBoundThreshold: Float
) : Mapper<List<DailyTransaction>, Map<LocalDate, Color>> {


    override fun map(from: List<DailyTransaction>): Map<LocalDate, Color> {
        val res = mutableMapOf<LocalDate, Color>()
        val dailyTransactions = from.sortedBy { it.primaryMinorUnitAmount }
        val size = from.size

        if (size == 0) return mapOf()

        val median = if (size % 2 == 0) {
            val left = dailyTransactions[size/2 - 1]
            val right = dailyTransactions[size/2]
            (left.primaryMinorUnitAmount + right.primaryMinorUnitAmount)/2
        } else {
            val middle = dailyTransactions[size/2]
            middle.primaryMinorUnitAmount
        }

        dailyTransactions.forEach { dailyTransaction ->
            val color = getColor(dailyTransaction.primaryMinorUnitAmount/median.toFloat())
            res[dailyTransaction.date] = color
        }

        return res
    }

    private fun getColor(medianRatio: Float): Color {
        val fraction = clip((medianRatio - lowerBoundThreshold)/(higherBoundThreshold-lowerBoundThreshold))
        return lerp(Level.One.color, Level.Four.color, fraction)
    }

    private fun clip(value: Float, min: Float = 0f, max: Float = 1f): Float {
        return min(max(value, min), max)
    }
}