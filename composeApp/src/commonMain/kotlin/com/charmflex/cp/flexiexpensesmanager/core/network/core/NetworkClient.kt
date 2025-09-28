package com.charmflex.cp.flexiexpensesmanager.core.network.core

import kotlin.reflect.KClass

interface NetworkClient {
    suspend fun <T: Any> get(endpoint: String, responseClass: KClass<T>, networkAttributes: List<NetworkAttribute<Any>>? = null): T

    suspend fun <T, R: Any> post(endpoint: String, body: T, responseClass: KClass<R>): R

    suspend fun <T, R: Any> patch(endpoint: String, body: T, responseClass: KClass<R>): R

    suspend fun <T, R: Any> delete(endpoint: String, body: T, responseClass: KClass<R>): R

    suspend fun <T, R: Any> put(endpoint: String, body: T, responseClass: KClass<R>): R
}

data class NetworkAttribute<out T : Any>(
    val name: String,
    val value: T,
)