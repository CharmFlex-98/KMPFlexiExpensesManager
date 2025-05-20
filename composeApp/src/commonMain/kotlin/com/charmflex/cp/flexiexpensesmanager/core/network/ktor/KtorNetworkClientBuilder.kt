package com.charmflex.cp.flexiexpensesmanager.core.network.ktor
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClientBuilder
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkInterceptor
import com.charmflex.cp.flexiexpensesmanager.core.network.exception.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

internal class KtorNetworkClientBuilder(
) : NetworkClientBuilder {
    private val httpClient: HttpClient = getClient()
    private var _baseUrl: String? = null

    override fun build(): NetworkClientBuilder.NetworkClient {
        return KtorNetworkClient(_baseUrl ?: "", httpClient)
    }

    override fun baseUrl(baseUrl: String): NetworkClientBuilder {
        return this.apply { _baseUrl = baseUrl.trimEnd('/')
        }
    }

    override fun addInterceptor(interceptor: NetworkInterceptor): NetworkClientBuilder {
        TODO("Not yet implemented")
    }

    private fun getClient(): HttpClient {
        return HttpClient {
            install(Logging) {
                logger = Logger.DEFAULT
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

    class KtorNetworkClient(
        private val baseUrl: String,
        private val httpClient: HttpClient
    ) : NetworkClientBuilder.NetworkClient {
        override suspend fun <T : Any> get(endpoint: String, responseClass: KClass<T>): T {
            val response = httpClient.get {
                append(endpoint)
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
            url("$baseUrl/${url.trimStart('/')}")
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
}

internal suspend inline fun <reified T : Any> NetworkClientBuilder.NetworkClient.get(endPoint: String) =
    this.get(endPoint, T::class)

internal suspend inline fun <T, reified U : Any> NetworkClientBuilder.NetworkClient.post(
    endPoint: String,
    body: T
) = this.post(endPoint, body, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClientBuilder.NetworkClient.patch(
    endPoint: String,
    body: T
) = this.patch(endPoint, body, U::class)

internal suspend inline fun <T, reified U : Any> NetworkClientBuilder.NetworkClient.delete(
    endPoint: String,
    body: T
) = this.delete(endPoint, body, U::class)
internal suspend inline fun <T, reified U : Any> NetworkClientBuilder.NetworkClient.put(
    endPoint: String,
    body: T
) = this.put(endPoint, body, U::class)

private fun getHttpException(httpStatusCode: Int) {

}




