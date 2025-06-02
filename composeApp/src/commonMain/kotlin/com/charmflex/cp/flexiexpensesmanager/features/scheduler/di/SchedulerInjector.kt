package com.charmflex.cp.flexiexpensesmanager.features.scheduler.di

import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.schedulerList.SchedulerListViewModel
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.SchedulerEditorViewModel
import com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor.SchedulerEditorViewModelFactory

internal interface SchedulerInjector {
    val schedulerListViewModel: SchedulerListViewModel
    val schedulerEditorViewModelFactory: SchedulerEditorViewModelFactory
//    fun workerFactory(): WorkerFactory
}