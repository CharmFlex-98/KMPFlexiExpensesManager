package com.charmflex.cp.flexiexpensesmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.features.session.SessionState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as AppComponentProvider).getAppComponent()

        val routeNavigator = appComponent.routeNavigator
        val sessionManager = appComponent.sessionManager

        sessionManager.updateSessionState(SessionState.Start)


        setContent {
            App(routeNavigator) {
                this.finish()
            }
        }
    }
}