package com.charmflex.cp.flexiexpensesmanager.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.annotation.Singleton

@Singleton
internal class ToastManager {
    private val _state: MutableStateFlow<ToastState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    fun postMessage(message: String) {
        post(message, toastType = ToastType.NEUTRAL)
    }

    fun postSuccess(message: String) {
        post(message, ToastType.SUCCESS)
    }

    fun postError(message: String?) {
        post(message ?: "Unknown error", ToastType.ERROR)
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