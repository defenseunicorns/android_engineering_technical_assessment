package com.defenseunicorns.flyaware.data.remote

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.TafDto

/*
* example metars url
* https://aviationweather.gov/api/data/metar?ids=KMCI%2CKORD%2CKBOS&format=json&taf=true
*/
interface AviationWeatherApi {
    suspend fun getMetars(icaoCodes: List<String>): List<MetarDto>
    suspend fun getTafs(icaoCodes: List<String>): List<TafDto>

    companion object {
        const val BASE_URL = "https://aviationweather.gov/api/data"

    }
}