package com.defenseunicorns.flyaware.data.repository

import com.defenseunicorns.flyaware.data.local.AirportDao
import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.data.remote.AviationWeatherApi
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.model.Metar
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlyAwareRepositoryImpl @Inject constructor(
    private val api: AviationWeatherApi,
    private val airportDao: AirportDao
) : FlyAwareRepository {

    override suspend fun getMetars(icaoCodes: List<String>): List<Metar> {
        return try {
            api.getMetars(icaoCodes).map { it.toMetar() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addAirport(icaoCode: String) {
        // TODO retrieve all airport information from the API and save it to the database
        airportDao.insertAirport(AirportEntity(icaoCode = icaoCode))
    }

    override suspend fun getSavedAirports(): Flow<List<AirportEntity>> {
        return airportDao.getAllAirports()
    }

    override suspend fun removeAirport(icaoCode: String) {
        airportDao.removeAirport(icaoCode)
    }
}
