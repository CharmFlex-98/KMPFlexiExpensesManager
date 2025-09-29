package com.charmflex.cp.flexiexpensesmanager.features.announcement.di
import com.charmflex.cp.flexiexpensesmanager.features.announcement.ui.AnnouncementViewModelFactory

internal interface AnnouncementInjector {
    fun announcementViewModelFactory(): AnnouncementViewModelFactory
}