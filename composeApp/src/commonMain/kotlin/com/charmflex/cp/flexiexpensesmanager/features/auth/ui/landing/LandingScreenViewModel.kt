package com.charmflex.flexiexpensesmanager.features.auth.ui.landing

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.flexiexpensesmanager.R
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.flexiexpensesmanager.core.navigation.routes.AuthRoutes
import com.charmflex.flexiexpensesmanager.core.navigation.routes.HomeRoutes
import com.charmflex.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.flexiexpensesmanager.core.tracker.UserData
import com.charmflex.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository.AuthRepository
import com.charmflex.flexiexpensesmanager.features.auth.event.AuthEventName
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.sign_in.SignInService
import com.charmflex.flexiexpensesmanager.features.auth.service.sign_in.SignInState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LandingScreenViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    private val signInService: SignInService,
    private val authRepository: AuthRepository,
    private val resourcesProvider: ResourcesProvider,
    private val eventTracker: EventTracker
) : ViewModel() {
    val snackBarState: MutableStateFlow<SnackBarState> =
        MutableStateFlow(SnackBarState.None)
    val landingViewState = MutableStateFlow(LandingViewState.default)


    fun onGuestLogin() {
        eventTracker.track(EventData.simpleEvent(AuthEventName.USER_ON_GUEST_LOGIN))
        proceedToHome()
    }


    private fun proceedToHome() {
        eventTracker.track(EventData.simpleEvent(AuthEventName.USER_NAVIGATE_HOME))
        routeNavigator.navigateAndPopUpTo(HomeRoutes.ROOT, AuthRoutes.LANDING)
    }

    private fun showLoading(loading: Boolean) {
        landingViewState.update {
            it.copy(
                isLoading = loading
            )
        }
    }

    fun onTrySignIn(context: Context) {
        showLoading(true)
        viewModelScope.launch {
            val state = signInService.trySignIn(context)
            when (state) {
                is SignInState.Success -> {
                    eventTracker.track(EventData.simpleEvent(AuthEventName.USER_TRY_SIGN_IN_SUCCEEDED))
                    handleGoogleSignInSuccess(state, true)
                }
                else -> {
                    eventTracker.track(EventData.simpleEvent(AuthEventName.USER_TRY_SIGN_IN_FAILED))
                    showLoading(false)
                }
            }
        }
    }

    fun onGoogleLogin(context: Context) {
        showLoading(true)
        viewModelScope.launch {
            when (val signInState = signInService.signIn(context)) {
                is SignInState.Success -> {
                    eventTracker.track(EventData.simpleEvent(AuthEventName.USER_SIGN_IN_SUCCEEDED))
                    handleGoogleSignInSuccess(signInState, false)
                }

                is SignInState.Failure -> {
                    eventTracker.track(EventData.simpleEvent(AuthEventName.USER_SIGN_IN_FAILED))
                    snackBarState.value =
                        SnackBarState.Error(signInState.message ?: "Generic login failure!")
                    showLoading(false)
                }
            }

        }
    }

    fun resetState() {
        snackBarState.value = SnackBarState.None

    }

    private suspend fun handleGoogleSignInSuccess(signInState: SignInState.Success, autoLogin: Boolean) {
        resultOf {
            authRepository.upsertUser(
                signInState.uid,
                signInState.username,
                signInState.email
            )
        }.onSuccess {
            onLoginSuccess(autoLogin, signInState)
        }.onFailure { e ->
            snackBarState.value = SnackBarState.Error(e.message)
            showLoading(false)
        }
    }

    private suspend fun onLoginSuccess(autoLogin: Boolean = false, signInState: SignInState.Success) {
        eventTracker.registerUser(UserData(signInState.uid, signInState.username ?: "Unknown", signInState.email ?: "Unknown"))
        landingViewState.update {
            it.copy(
                isLoading = false,
                loginSuccessViewState = LandingViewState.LoginSuccessViewState(
                    title = resourcesProvider.getString(if (autoLogin) R.string.login_identity_verified_success else R.string.login_success),
                    message = resourcesProvider.getString(R.string.login_redirect_subtitle)
                )
            )
        }

        delay(3000)
        proceedToHome()
    }
}

internal data class LandingViewState(
    val isLoading: Boolean,
    val loginSuccessViewState: LoginSuccessViewState?
) {
    data class LoginSuccessViewState(
        val title: String,
        val message: String
    )
    companion object {
        val default = LandingViewState(isLoading = false, loginSuccessViewState = null)
    }
}