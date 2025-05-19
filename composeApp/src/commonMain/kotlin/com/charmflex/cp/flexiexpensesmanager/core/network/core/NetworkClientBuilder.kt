package com.charmflex.cp.flexiexpensesmanager.core.network.core

import kotlin.reflect.KClass

internal interface NetworkClientBuilder {
    interface NetworkClient {
        suspend fun <T: Any> get(endpoint: String, responseClass: KClass<T>): T

        suspend fun <T, R: Any> post(endpoint: String, body: T, responseClass: KClass<R>): R

        suspend fun <T, R: Any> patch(endpoint: String, body: T, responseClass: KClass<R>): R

        suspend fun <T, R: Any> delete(endpoint: String, body: T, responseClass: KClass<R>): R

        suspend fun <T, R: Any> put(endpoint: String, body: T, responseClass: KClass<R>): R

    }


    fun build(): NetworkClient
    fun baseUrl(baseUrl: String): NetworkClientBuilder
    fun addInterceptor(interceptor: NetworkInterceptor): NetworkClientBuilder
}