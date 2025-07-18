package com.charmflex.cp.flexiexpensesmanager.features.tag.di

import com.charmflex.cp.flexiexpensesmanager.features.tag.ui.TagSettingViewModel

internal interface TagInjector {
    fun tagSettingViewModel(): TagSettingViewModel
}