package com.defenseunicorns.flyaware.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirport(airport: AirportEntity)

    @Query("DELETE FROM airports WHERE icaoCode = :icaoCode" )
    suspend fun removeAirport(icaoCode: String)

    @Query("SELECT * FROM airports")
    fun getAllAirports(): Flow<List<AirportEntity>>
    
    @Query("SELECT * FROM airports WHERE icaoCode = :icaoCode")
    suspend fun getAirportByIcaoCode(icaoCode: String): AirportEntity?
    
    @Query("SELECT EXISTS(SELECT 1 FROM airports WHERE icaoCode = :icaoCode)")
    suspend fun airportExists(icaoCode: String): Boolean
    
    @Update
    suspend fun updateAirport(airport: AirportEntity)
    
    @Query("DELETE FROM airports")
    suspend fun clearAllAirports()
}
