package com.defenseunicorns.flyaware.data.remote

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.TafDto
import com.defenseunicorns.flyaware.data.remote.dto.AirportInfoDto

/**
 * Remote data source interface for aviation weather API operations.
 * Provides abstraction for all remote data operations.
 */
interface RemoteDataSource {
    
    /**
     * Fetches METAR data for the given ICAO airport codes.
     * @param icaoCodes List of ICAO airport codes
     * @return Result containing list of METAR DTOs or error
     */
    suspend fun getMetars(icaoCodes: List<String>): Result<List<MetarDto>>
    
    /**
     * Fetches TAF data for the given ICAO airport codes.
     * @param icaoCodes List of ICAO airport codes
     * @return Result containing list of TAF DTOs or error
     */
    suspend fun getTafs(icaoCodes: List<String>): Result<List<TafDto>>
    
    /**
     * Fetches airport information for the given ICAO code.
     * @param icaoCode ICAO airport code
     * @return Result containing airport info DTO or error
     */
    suspend fun getAirportInfo(icaoCode: String): Result<AirportInfoDto>
    
    /**
     * Validates if an ICAO code exists in the aviation database.
     * @param icaoCode ICAO airport code to validate
     * @return Result containing boolean indicating if airport exists
     */
    suspend fun validateAirportCode(icaoCode: String): Result<Boolean>
} 