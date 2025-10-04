package com.charmflex.cp.flexiexpensesmanager.core.network.ktor

import coil3.intercept.Interceptor
import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.app.DeviceInfoProvider
import com.charmflex.cp.flexiexpensesmanager.core.crypto.SignatureVerifier
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkAttribute
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClient
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkInterceptor
import com.charmflex.cp.flexiexpensesmanager.core.network.core.interceptors.CommonHeaderInjector
import com.charmflex.cp.flexiexpensesmanager.core.network.core.interceptors.SignatureCheckerInterceptor
import com.charmflex.cp.flexiexpensesmanager.core.network.exception.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.annotation.Singleton
import kotlin.reflect.KClass

internal object InvalidSignature : Exception()

internal class KtorNetworkClientBuilder(
    private val appConfigProvider: AppConfigProvider
) : NetworkClient.Builder<HttpRequestBuilder, HttpClientCall> {
    private var baseUrl: String? = ""
    private val interceptors: MutableList<NetworkInterceptor<HttpRequestBuilder, HttpClientCall>> = mutableListOf()


    override fun interceptor(interceptor: NetworkInterceptor<HttpRequestBuilder, HttpClientCall>): NetworkClient.Builder<HttpRequestBuilder, HttpClientCall> {
        this.interceptors.add(interceptor)
        return this
    }

    override fun build(): NetworkClient {
        return KtorNetworkClient(
            appConfigProvider,
            this.interceptors
        )
    }
}

internal class KtorNetworkClient(
    appConfigProvider: AppConfigProvider,
    private val interceptors: List<NetworkInterceptor<HttpRequestBuilder, HttpClientCall>>
) : NetworkClient {
    private val baseUrl = appConfigProvider.baseUrl()
    private var httpClient: HttpClient

    private val _interceptors: MutableList<NetworkInterceptor<HttpRequestBuilder, HttpClientCall>> = mutableListOf()

    init {
        httpClient = getClient()
        initInterceptors()
    }

    private fun initInterceptors() {
        interceptors.forEach { addInterceptor(it) }
    }

    private fun getClient(): HttpClient {
        val client =  HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("==========")
                        println("HTTP::$message")
                        println("==========")
                    }
                }
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
        }
        client.plugin(HttpSend).intercept { request ->
            val chain = NetworkInterceptor.InterceptorChain(_interceptors) {
                execute(request)
            }

            chain.proceed(request)
        }

        return client
    }

    override suspend fun <T : Any> get(
        endpoint: String,
        responseClass: KClass<T>,
        networkAttributes: List<NetworkAttribute<Any>>?
    ): T {
        val response = httpClient.get {
            append(endpoint)
            networkAttributes?.forEach {
                attributes.put(AttributeKey(it.name), it.value)
            }
        }
        return decodeResponse(response, responseClass)
    }

    override suspend fun <T: Any, R : Any> post(
        endpoint: String,
        body: T,
        requestClass: KClass<T>,
        responseClass: KClass<R>,
        networkAttributes: List<NetworkAttribute<Any>>?
    ): R {
        val response = httpClient.post {
            append(endpoint)
            setBody(body, TypeInfo(requestClass))
            networkAttributes?.forEach {
                attributes.put(AttributeKey(it.name), it.value)
            }
        }
        return decodeResponse(response, responseClass)
    }

    override suspend fun <T : Any, R : Any> patch(
        endpoint: String,
        body: T,
        requestClass: KClass<T>,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.patch {
            append(endpoint)
            setBody(body, TypeInfo(requestClass))
        }
        return decodeResponse(response, responseClass)
    }

    override suspend fun <T, R : Any> delete(
        endpoint: String,
        body: T,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.delete {
            append(endpoint)
        }
        return decodeResponse(response, responseClass)
    }

    override suspend fun <T : Any, R : Any> put(
        endpoint: String,
        body: T,
        requestClass: KClass<T>,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.put {
            append(endpoint)
            setBody(body, TypeInfo(requestClass))
        }
        return decodeResponse(response, responseClass)
    }

    private fun addInterceptor(interceptor: NetworkInterceptor<HttpRequestBuilder, HttpClientCall>) {
        _interceptors.add(interceptor)
    }

    private fun HttpRequestBuilder.append(url: String) {
        if (url.contains("https")) {
            url(url.trimStart('/'))
        } else {
            url("$baseUrl/${url.trimStart('/')}")
        }
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
    }

    @OptIn(InternalSerializationApi::class)
    private suspend fun <T : Any> decodeResponse(
        response: HttpResponse,
        responseClass: KClass<T>
    ): T {
        if (response.status.isSuccess().not()) {
            throw ApiException(response.status.value, response.bodyAsText())
        }
        val text = response.bodyAsText()

        return Json.decodeFromString(responseClass.serializer(), text)
    }
}

internal suspend inline fun <reified T : Any> NetworkClient.get(endPoint: String, attributesBuilder: MutableList<NetworkAttribute<Any>>.() -> Unit): T {
    val attrs = mutableListOf<NetworkAttribute<Any>>()
        .apply {
            attributesBuilder()
        }
    return this.get(endPoint, T::class, attrs.toList())
}

internal suspend inline fun <reified T : Any, reified U : Any> NetworkClient.post(
    endPoint: String,
    body: T,
    attributesBuilder: MutableList<NetworkAttribute<Any>>.() -> Unit
): U? {
    val attrs = mutableListOf<NetworkAttribute<Any>>()
        .apply {
            attributesBuilder()
        }
    return this.post(endPoint, body, T::class, U::class, attrs)
}

internal suspend inline fun <reified T : Any, reified U : Any> NetworkClient.patch(
    endPoint: String,
    body: T
) = this.patch(endPoint, body, T::class, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClient.delete(
    endPoint: String,
    body: T
) = this.delete(endPoint, body, U::class)

internal suspend inline fun <reified T : Any, reified U : Any> NetworkClient.put(
    endPoint: String,
    body: T
) = this.put(endPoint, body, T::class, U::class)

private fun getHttpException(httpStatusCode: Int) {

}




