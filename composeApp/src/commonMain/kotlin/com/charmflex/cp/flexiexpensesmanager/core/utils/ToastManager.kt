package com.charmflex.cp.flexiexpensesmanager.core.utils

import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventData
import com.charmflex.cp.flexiexpensesmanager.core.tracker.EventTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton

@Singleton
internal class ToastManager(
    private val eventTracker: EventTracker
) {
    private val _state: MutableStateFlow<ToastState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    fun postMessage(message: String) {
        post(message, toastType = ToastType.NEUTRAL)
    }

    fun postSuccess(message: String) {
        eventTracker.track(EventData.event(UtilEventName.TOAST_SUCCESS, mapOf("message" to message)))
        post(message, ToastType.SUCCESS)
    }

    fun postError(message: String?) {
        val msg = message ?: "Unknown error"
        eventTracker.track(EventData.event(UtilEventName.TOAST_ERROR, mapOf("message" to msg)))
        post(msg, ToastType.ERROR)
    }

    private fun post(message: String, toastType: ToastType) {
        _state.value = ToastState(message, toastType)
    }

    fun reset() {
        _state.value = null
    }
}

internal data class ToastState(
    val message: String,
    val toastType: ToastType
)

enum class ToastType {
    SUCCESS, ERROR, NEUTRAL
}