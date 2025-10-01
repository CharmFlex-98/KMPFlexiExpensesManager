package com.charmflex.cp.flexiexpensesmanager.core.network.core.interceptors

import com.charmflex.cp.flexiexpensesmanager.core.crypto.SignatureVerifier
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkInterceptor
import com.charmflex.cp.flexiexpensesmanager.core.network.core.peek
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.InvalidSignature
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.observer.wrapWithContent
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.util.AttributeKey
import org.koin.core.annotation.Singleton

@Singleton
internal class SignatureCheckerInterceptor(
    private val signatureVerifier: SignatureVerifier
) : NetworkInterceptor<HttpRequestBuilder, HttpClientCall> {
    override suspend fun intercept(
        request: HttpRequestBuilder,
        chain: NetworkInterceptor.InterceptorChain<HttpRequestBuilder, HttpClientCall>
    ): HttpClientCall {
        val responseWrapper = chain.proceed(request)
        val (body, newResponseWrapper) = responseWrapper.peek()

        if (!validatePayload(responseWrapper.response, body)) {
            throw InvalidSignature
        }

        return newResponseWrapper
    }

    private fun validatePayload(response: HttpResponse, payload: String): Boolean {
        val signature = response.headers["X-Signature"]
        val verifySignature = response.call.request.attributes.getOrNull(AttributeKey("verifySignature")) == true

        if (!verifySignature) {
            return false
        }

        if (signature.isNullOrBlank()) {
            throw InvalidSignature
        }

        val isPayloadValid = signatureVerifier.verify(payload, signature)

        if (!isPayloadValid) {
            throw InvalidSignature
        }

        return true
    }

}