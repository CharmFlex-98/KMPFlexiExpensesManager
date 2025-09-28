package com.charmflex.cp.flexiexpensesmanager.features.currency.data.remote.api
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkAttributes
import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClient
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.get
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.models.CurrencyRateResponse
import org.koin.core.annotation.Singleton

internal interface CurrencyApi {
    suspend fun getLatestCurrencyRate(): CurrencyRateResponse
}


@Singleton
internal class CurrencyApiImpl (
    private val networkClient: NetworkClient,
) : CurrencyApi {
    override suspend fun getLatestCurrencyRate(): CurrencyRateResponse {
        return networkClient.get("/api/v1/currency/latest") {
//            add(NetworkAttributes.verifySignature)
        }
    }
}