package com.defenseunicorns.flyaware.data

import android.util.Log
import com.defenseunicorns.flyaware.database.MetarDao
import com.defenseunicorns.flyaware.database.MetarDto
import com.defenseunicorns.flyaware.network.AviationWeatherClient
import javax.inject.Inject

class FetchMetarUseCase @Inject constructor(
    private val client: AviationWeatherClient,
    private val metarDao: MetarDao
) {
    suspend operator fun invoke(id: String) {
        try {
            val metar = client.getMetar(id)
            metarDao.insert(MetarDto.from(metar))
        } catch (e: Exception) {
            // TODO propagate error
            Log.e("tag", "FetchMetar failed", e)
        }
    }
}