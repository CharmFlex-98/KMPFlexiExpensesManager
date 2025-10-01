package com.charmflex.cp.flexiexpensesmanager.core.network.core

import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.observer.wrapWithContent
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.split
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

internal suspend fun HttpClientCall.peek() : Pair<String, HttpClientCall> {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val response = this.response
    val (first, second) = response.bodyAsChannel().split(coroutineScope)

    val body = first.readRemaining().readText()
    val newResponse = response.call.wrapWithContent(second)

    return body to newResponse
}