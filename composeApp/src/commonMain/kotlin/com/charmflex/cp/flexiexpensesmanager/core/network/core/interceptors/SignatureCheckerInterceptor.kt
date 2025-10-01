package com.charmflex.cp.flexiexpensesmanager.core.network.core.interceptors

import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkInterceptor
import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.HttpRequestBuilder

internal class SignatureCheckerInterceptor : NetworkInterceptor<HttpRequestBuilder, HttpClientCall> {
    override suspend fun intercept(
        request: HttpRequestBuilder,
        chain: NetworkInterceptor.InterceptorChain<HttpRequestBuilder, HttpClientCall>
    ): HttpClientCall {
        val response = chain.proceed(request)
        // TODO

        return response
    }
}