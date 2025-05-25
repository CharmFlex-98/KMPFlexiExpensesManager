package com.charmflex.flexiexpensesmanager.core.scheduler

import com.charmflex.cp.flexiexpensesmanager.features.scheduler.domain.models.SchedulerDomainModel

internal interface FEScheduler<T: SchedulerDomainModel> {
    fun schedule(model: T)

    fun unSchedule(id: Int)
}