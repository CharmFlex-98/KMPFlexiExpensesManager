package com.charmflex.cp.flexiexpensesmanager.features.currency.data.repositories

import com.charmflex.cp.flexiexpensesmanager.core.network.core.NetworkClientBuilder
import com.charmflex.cp.flexiexpensesmanager.core.network.ktor.get
import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorage
import com.charmflex.cp.flexiexpensesmanager.core.utils.datetime.localDateTimeNow
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.local.CurrencyKeyStorage
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.models.CurrencyData
import com.charmflex.cp.flexiexpensesmanager.features.currency.domain.repositories.CurrencyRepository
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.models.CurrencyRateResponse
import io.fluidsonic.currency.Currency
import io.ktor.util.logging.Logger
import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

internal class CurrencyRepositoryImpl constructor(
    private val fileStorage: FileStorage,
    private val currencyKeyStorage: CurrencyKeyStorage,
    private val networkClient: NetworkClientBuilder.NetworkClient,
    private val assetReader: AssetReader
) : CurrencyRepository {

    override suspend fun fetchLatestCurrencyRates(): CurrencyData {
        val res: CurrencyRateResponse = networkClient.get("https://api.fxratesapi.com/latest")
        val item = CurrencyData(
            timestamp = res.timestamp,
            date = res.date,
            base = res.base,
            currencyRates = res.rates.mapNotNull { (code, rate) ->
                if (code.length != 3) return@mapNotNull null

                val currency = try {
                    Currency.forCode(code)
                } catch (e: Exception) {
                    return@mapNotNull null
                }

                code to CurrencyData.Currency(
                    code,
                    currency.defaultFractionDigits,
                    rate.toFloat()
                )
            }.toMap()
        )
        setLatestCurrencyRates(item)
        return item
    }

    private fun mapNetworkResponse(res: CurrencyRateResponse): CurrencyData {
        return CurrencyData(
            timestamp = res.timestamp,
            date = res.date,
            base = res.base,
            currencyRates = res.rates.mapNotNull { (code, rate) ->
                if (code.length != 3) return@mapNotNull null

                val currency = try {
                    Currency.forCode(code)
                } catch (e: Exception) {
                    return@mapNotNull null
                }

                code to CurrencyData.Currency(
                    code,
                    currency.defaultFractionDigits,
                    rate.toFloat()
                )
            }.toMap()
        )
    }

    private suspend fun setLatestCurrencyRates(currencyData: CurrencyData) {
        val json = Json.encodeToString(currencyData)
        fileStorage.write(CURRENCY_FILE_NAME, json.toByteArray())
        currencyKeyStorage.setLastCurrencyRateUpdateTimestamp(localDateTimeNow())
    }

    override suspend fun getCacheCurrencyRates(): CurrencyData? {
        return try {
            val res = fileStorage.read(CURRENCY_FILE_NAME)
            Json.decodeFromString<CurrencyData>(res)
        } catch (e: Exception) {
            // alternative
            val data = Json.decodeFromString<CurrencyRateResponse>(assetReader.read("currency_rate_fallback.json"))
            return mapNetworkResponse(data)
        }
    }

    override suspend fun setLastCurrencyRateUpdateTimestamp(localDateTime: LocalDateTime) {
        currencyKeyStorage.setLastCurrencyRateUpdateTimestamp(localDateTime)
    }

    override suspend fun getLastCurrencyRateUpdateTimestamp(): LocalDateTime? {
        return currencyKeyStorage.getLastCurrencyRateUpdateTimestamp()
    }
}

private const val CURRENCY_FILE_NAME = "currency_rate.txt"