package com.defenseunicorns.flyaware.data.remote

import android.util.Log
import com.defenseunicorns.flyaware.data.remote.AviationWeatherApi.Companion.BASE_URL
import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.TafDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class AviationWeatherApiImpl @Inject constructor(
    private val client: HttpClient
) : AviationWeatherApi {

    override suspend fun getMetars(icaoCodes: List<String>): List<MetarDto> {
        val url = "$BASE_URL/metar?ids=${icaoCodes.joinToString(",")}&format=json"
        Log.e("URL", url)
        val response = client.get(url)
        Log.e("RESPONSE", response.toString())
        var body : List<MetarDto>
        try {
            body = response.body()
        } catch (e: Exception) {
            Log.e("EXCEPTION", e.toString())
            body = emptyList()
        }
        Log.e("BODY", body.toString())
        return body
    }

    override suspend fun getTafs(icaoCodes: List<String>): List<TafDto> {
        val url = "$BASE_URL/taf?ids=${icaoCodes.joinToString(",")}&format=json"
        return client.get(url).body()
    }
}