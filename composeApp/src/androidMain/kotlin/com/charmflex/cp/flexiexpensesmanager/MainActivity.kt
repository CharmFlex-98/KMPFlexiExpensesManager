package com.charmflex.cp.flexiexpensesmanager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.charmflex.cp.flexiexpensesmanager.di.AppComponentProvider
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.features.billing.AndroidBillingManager
import com.charmflex.cp.flexiexpensesmanager.features.billing.BillingManager
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

        sessionManager.updateSessionState(SessionState.Start)

        val billingManager = AndroidBillingManager(this)
        appComponent.setBillingManager(billingManager)

        scope.launch {
            billingManager.initialize()
        }

        setContent {
            App(routeNavigator) {
                this.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as AppComponentProvider).getAppComponent().getBillingManager().cleanup()
    }
}