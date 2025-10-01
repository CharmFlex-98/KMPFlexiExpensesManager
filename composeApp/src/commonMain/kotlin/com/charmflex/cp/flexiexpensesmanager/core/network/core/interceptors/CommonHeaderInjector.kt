package com.charmflex.cp.flexiexpensesmanager.core.network.core.interceptors
import com.charmflex.cp.flexiexpensesmanager.core.app.DeviceInfoProvider
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkInterceptor
import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.HttpRequestBuilder
import org.koin.core.annotation.Singleton

@Singleton
internal class CommonHeaderInjector(
    private val deviceInfoProvider: DeviceInfoProvider
) : NetworkInterceptor<HttpRequestBuilder, HttpClientCall> {
    override suspend fun intercept(
        request: HttpRequestBuilder,
        chain: NetworkInterceptor.InterceptorChain<HttpRequestBuilder, HttpClientCall>
    ): HttpClientCall {
        val locale = deviceInfoProvider.locale()
        request.headers.append("Accept-Language", locale)
        return chain.proceed(request)
    }
}