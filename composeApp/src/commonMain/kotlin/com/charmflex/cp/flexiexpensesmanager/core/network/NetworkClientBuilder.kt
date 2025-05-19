package com.charmflex.cp.flexiexpensesmanager.core.network
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


interface NetworkClientBuilder {
    fun addInterceptor(interceptor: Interceptor): NetworkClientBuilder

    fun <T: Any> buildApi(c: KClass<T>): T

    fun buildRetrofit(): NetworkClient

}

// TODO: Implementation
class DefaultNetworkClientBuilder(
    private val interceptors: MutableList<Interceptor> = mutableListOf(),
) : NetworkClientBuilder {
    override fun addInterceptor(interceptor: Interceptor): NetworkClientBuilder {
        TODO("Not yet implemented")
    }

    override fun <T : Any> buildApi(c: KClass<T>): T {
        TODO("Not yet implemented")
    }

    override fun buildRetrofit(): NetworkClient {
        TODO("Not yet implemented")
    }

}