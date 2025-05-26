package com.charmflex.cp.flexiexpensesmanager.features.home.ui.dashboard

import androidx.lifecycle.ViewModel
import com.charmflex.cp.flexiexpensesmanager.features.home.ui.HomeItemRefreshable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Factory

@Factory
internal class DashboardViewModel constructor() : ViewModel(), HomeItemRefreshable {
    private val _plugins = MutableStateFlow<List<DashboardPlugin>>(emptyList())
    val plugins = _plugins.asStateFlow()

    fun initPlugins(plugins: List<DashboardPlugin>) {
        _plugins.update { plugins }
    }

    override fun refreshHome() {
        _plugins.value.forEach {
            it.refresh()
        }
    }
}