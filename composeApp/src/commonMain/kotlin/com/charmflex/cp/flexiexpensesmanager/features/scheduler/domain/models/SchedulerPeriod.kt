package com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models

import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import org.jetbrains.compose.resources.StringResource

internal enum class SchedulerPeriod {
    DAILY, MONTHLY, YEARLY;

    companion object {
        fun getStringRes(schedulerPeriod: SchedulerPeriod): StringResource {
            return when (schedulerPeriod) {
                SchedulerPeriod.DAILY -> Res.string.generic_daily
                SchedulerPeriod.MONTHLY -> Res.string.generic_monthly
                SchedulerPeriod.YEARLY -> Res.string.generic_yearly
            }
        }
    }
}