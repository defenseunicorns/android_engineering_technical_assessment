package com.defenseunicorns.flyaware.network

import com.defenseunicorns.flyaware.core.model.Airport
import com.defenseunicorns.flyaware.network.model.AirportResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AviationWeatherClient @Inject constructor(
    private val httpClient: HttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getAirport(id: String): Airport {
        val response = httpClient.get("$URL/data/airport") {
            parameter("ids", id)
            parameter("format", "json")
        }

        val airports: List<AirportResponse> = json.decodeFromString(response.bodyAsText())

        return airports.first().asCoreModel()
    }

    companion object {
        private const val URL = "https://aviationweather.gov/api"
    }
}