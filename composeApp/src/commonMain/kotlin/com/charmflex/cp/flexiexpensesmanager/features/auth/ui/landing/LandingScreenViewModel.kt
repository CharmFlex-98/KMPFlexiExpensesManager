package com.charmflex.cp.flexiexpensesmanager.features.auth.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charmflex.cp.flexiexpensesmanager.core.navigation.RouteNavigator
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.AuthRoutes
import com.charmflex.cp.flexiexpensesmanager.core.navigation.routes.HomeRoutes
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import com.charmflex.cp.flexiexpensesmanager.core.tracker.UserData
import com.charmflex.cp.flexiexpensesmanager.core.utils.ResourcesProvider
import com.charmflex.cp.flexiexpensesmanager.core.utils.resultOf
import com.charmflex.cp.flexiexpensesmanager.features.auth.domain.repository.AuthRepository
import com.charmflex.cp.flexiexpensesmanager.features.auth.event.AuthEventName
import com.charmflex.cp.flexiexpensesmanager.features.auth.service.sign_in.SignInService
import com.charmflex.flexiexpensesmanager.features.auth.service.sign_in.SignInState
import com.charmflex.cp.flexiexpensesmanager.ui_common.SnackBarState
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class LandingScreenViewModel(
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
        routeNavigator.navigateAndPopUpTo(HomeRoutes.ROOT, AuthRoutes.Landing)
    }

    private fun showLoading(loading: Boolean) {
        landingViewState.update {
            it.copy(
                isLoading = loading
            )
        }
    }

    fun onTrySignIn() {
        showLoading(true)
        viewModelScope.launch {
            val state = signInService.trySignIn()
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

    fun onGoogleLogin() {
        showLoading(true)
        viewModelScope.launch {
            when (val signInState = signInService.signIn()) {
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
                    title = resourcesProvider.getString(if (autoLogin) Res.string.login_identity_verified_success else Res.string.login_success),
                    message = resourcesProvider.getString(Res.string.login_redirect_subtitle)
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