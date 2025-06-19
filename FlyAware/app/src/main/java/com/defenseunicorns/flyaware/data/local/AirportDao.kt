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
}
