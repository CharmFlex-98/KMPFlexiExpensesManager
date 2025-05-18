package com.charmflex.cp.flexiexpensesmanager.features.scheduler.di

import com.charmflex.flexiexpensesmanager.features.scheduler.ui.schedulerList.SchedulerListViewModel
import com.charmflex.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.SchedulerEditorViewModel

internal interface SchedulerInjector {
    val schedulerListViewModel: SchedulerListViewModel
    val schedulerEditorViewModelFactory: SchedulerEditorViewModel.Factory
//    fun workerFactory(): WorkerFactory
}