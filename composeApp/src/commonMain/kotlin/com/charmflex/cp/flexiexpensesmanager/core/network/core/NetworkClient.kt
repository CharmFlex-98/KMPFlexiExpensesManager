package com.charmflex.cp.flexiexpensesmanager.core.network.core

import kotlin.reflect.KClass

internal interface NetworkClient <REQ, RES> {
    val interceptor: List<NetworkInterceptor<REQ, RES>>

    suspend fun <T: Any> get(endpoint: String, responseClass: KClass<T>, networkAttributes: List<NetworkAttribute<Any>>? = null): T

    suspend fun <T : Any, R: Any> post(endpoint: String, body: T, requestClass: KClass<T>, responseClass: KClass<R>, networkAttributes: List<NetworkAttribute<Any>>? = null): R

    suspend fun <T: Any, R: Any> patch(endpoint: String, body: T, requestClass: KClass<T>, responseClass: KClass<R>): R

    suspend fun <T, R: Any> delete(endpoint: String, body: T, responseClass: KClass<R>): R

    suspend fun <T : Any, R: Any> put(endpoint: String, body: T, requestClass: KClass<T>, responseClass: KClass<R>): R
}

data class NetworkAttribute<out T : Any>(
    val name: String,
    val value: T,
)