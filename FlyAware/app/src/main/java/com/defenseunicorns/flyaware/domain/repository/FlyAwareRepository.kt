package com.defenseunicorns.flyaware.domain.repository

import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.model.Metar
import kotlinx.coroutines.flow.Flow


interface FlyAwareRepository {

    /**
     * Get the latest decoded METAR reports for the given ICAO airport codes.
     */
    suspend fun getMetars(icaoCodes: List<String>): List<Metar>

    suspend fun addAirport(icaoCode: String)

    suspend fun getSavedAirports(): Flow<List<AirportEntity>>

    suspend fun removeAirport(icaoCode: String)
}
