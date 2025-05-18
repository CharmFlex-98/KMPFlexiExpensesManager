package com.charmflex.cp.flexiexpensesmanager

import androidx.compose.ui.window.ComposeUIViewController
import com.charmflex.cp.flexiexpensesmanager.di.AppComponent
import com.charmflex.cp.flexiexpensesmanager.di.KoinInitializer
import org.koin.core.component.KoinComponent
import platform.Foundation.NSData
import platform.posix.exit

fun MainViewController() = ComposeUIViewController {
    KoinInitializer().init()
    val appComponent = AppComponent()
    App(appComponent.routeNavigator) {
        exit(0)
    }
}