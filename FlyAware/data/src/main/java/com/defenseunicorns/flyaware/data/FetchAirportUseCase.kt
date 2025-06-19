package com.defenseunicorns.flyaware.data

import android.util.Log
import com.defenseunicorns.flyaware.database.AirportDao
import com.defenseunicorns.flyaware.database.AirportDto
import com.defenseunicorns.flyaware.network.AviationWeatherClient
import javax.inject.Inject

class FetchAirportUseCase @Inject constructor(
    private val client: AviationWeatherClient,
    private val airportDao: AirportDao
) {
    suspend operator fun invoke(id: String) {
        try {
            val airport = client.getAirport(id)
            airportDao.insert(AirportDto.from(airport))
        } catch (e: Exception) {
            // TODO propagate error
            Log.e("tag", "AddAirport failed", e)
        }
    }
}
