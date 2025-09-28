package com.charmflex.cp.flexiexpensesmanager.core.network.ktor

import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.core.crypto.SignatureVerifier
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkAttribute
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkAttributes
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClient
import com.charmflex.cp.flexiexpensesmanager.core.network.exception.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import io.ktor.utils.io.charsets.Charsets
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.koin.core.annotation.Singleton
import kotlin.reflect.KClass

internal object InvalidSignature : Exception()

@Singleton
internal class KtorNetworkClient(
    appConfigProvider: AppConfigProvider,
    private val signatureVerifier: SignatureVerifier,
) : NetworkClient {
    private val baseUrl = appConfigProvider.baseUrl()
    private var httpClient: HttpClient

    init {
        httpClient = getClient()
        initInterceptors()
    }

    private fun initInterceptors() {
        httpClient.receivePipeline.intercept(HttpReceivePipeline.After) {
            val response: HttpResponse = subject
            val signature = response.headers["X-Signature"]
            val verifySignature = response.call.request.attributes.getOrNull(AttributeKey("verifySignature")) == true

            if (!verifySignature) {
                proceedWith(response)
                return@intercept
            }

            if (signature.isNullOrBlank()) {
                throw InvalidSignature
            }

            val payload = response.bodyAsText(Charsets.UTF_8)
            val isPayloadValid = signatureVerifier.verify(payload, signature)

            if (!isPayloadValid) {
                throw InvalidSignature
            }

            proceedWith(response)
        }
    }

    private fun getClient(): HttpClient {
        return HttpClient {
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

    override suspend fun <T, R : Any> post(
        endpoint: String,
        body: T,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.post {
            append(endpoint)
        }
        return decodeResponse(response, responseClass)
    }

    override suspend fun <T, R : Any> patch(
        endpoint: String,
        body: T,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.patch {
            append(endpoint)
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

    override suspend fun <T, R : Any> put(
        endpoint: String,
        body: T,
        responseClass: KClass<R>
    ): R {
        val response = httpClient.put {
            append(endpoint)
        }
        return decodeResponse(response, responseClass)
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

internal suspend inline fun <T, reified U : Any> NetworkClient.post(
    endPoint: String,
    body: T
) = this.post(endPoint, body, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClient.patch(
    endPoint: String,
    body: T
) = this.patch(endPoint, body, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClient.delete(
    endPoint: String,
    body: T
) = this.delete(endPoint, body, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClient.put(
    endPoint: String,
    body: T
) = this.put(endPoint, body, U::class)

private fun getHttpException(httpStatusCode: Int) {

}




