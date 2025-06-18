package com.defenseunicorns.flyaware.data.repository

import android.util.Log
import com.defenseunicorns.flyaware.data.remote.AviationWeatherApi
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.model.Metar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlyAwareRepositoryImpl @Inject constructor(
    private val api: AviationWeatherApi
) : FlyAwareRepository {

    override suspend fun getMetars(icaoCodes: List<String>): List<Metar> {
        return try {
            println("getMetars from repository: $icaoCodes")
            api.getMetars(icaoCodes).map { it.toMetar() }
        } catch (e: Exception) {
            Log.e("FlyAwareRepositoryImpl", "getMetars: $e")
            emptyList()
        }
    }
}
