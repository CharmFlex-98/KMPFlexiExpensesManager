package com.charmflex.cp.flexiexpensesmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.model.AndroidBillingInitOptions
import com.charmflex.cp.flexiexpensesmanager.features.session.SessionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (application as AppComponentProvider).getAppComponent()


        val routeNavigator = appComponent.routeNavigator()
        val sessionManager = appComponent.sessionManager()
        val toastManager = appComponent.toastManager()

        sessionManager.updateSessionState(SessionState.Start)


        setContent {
            App(routeNavigator, toastManager) {
                this.finish()
            }
        }
    }
}