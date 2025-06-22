package com.defenseunicorns.flyaware.data.local

import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for database operations.
 * Provides abstraction for all local data operations.
 */
interface LocalDataSource {
    
    /**
     * Inserts an airport into the local database.
     * @param airport The airport entity to insert
     */
    suspend fun insertAirport(airport: AirportEntity)
    
    /**
     * Removes an airport from the local database by ICAO code.
     * @param icaoCode The ICAO code of the airport to remove
     */
    suspend fun removeAirport(icaoCode: String)
    
    /**
     * Gets all saved airports from the local database.
     * @return Flow of list of airport entities
     */
    fun getAllAirports(): Flow<List<AirportEntity>>
    
    /**
     * Gets a specific airport by ICAO code.
     * @param icaoCode The ICAO code of the airport
     * @return The airport entity if found, null otherwise
     */
    suspend fun getAirportByIcaoCode(icaoCode: String): AirportEntity?
    
    /**
     * Checks if an airport exists in the local database.
     * @param icaoCode The ICAO code to check
     * @return True if the airport exists, false otherwise
     */
    suspend fun airportExists(icaoCode: String): Boolean
    
    /**
     * Updates an existing airport in the local database.
     * @param airport The airport entity with updated information
     */
    suspend fun updateAirport(airport: AirportEntity)
    
    /**
     * Clears all airports from the local database.
     */
    suspend fun clearAllAirports()
} 