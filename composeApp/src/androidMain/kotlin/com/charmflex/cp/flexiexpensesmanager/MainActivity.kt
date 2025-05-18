package com.charmflex.cp.flexiexpensesmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.flexiexpensesmanager.features.session.SessionState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as AppComponentProvider).getAppComponent()

        val routeNavigator = appComponent.routeNavigator
        val eventTracker = appComponent.eventTracker
        val sessionManager = appComponent.sessionManager

        sessionManager.updateSessionState(SessionState.Start)

        // Test
        eventTracker.track(EventData.simpleEvent("test-event"))

        setContent {
            App(routeNavigator) {
                this.finish()
            }
        }
    }
}