package com.defenseunicorns.flyaware.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirport(airport: AirportEntity)

    @Delete
    suspend fun deleteAirport(airport: AirportEntity)

    @Query("SELECT * FROM airports")
    fun getAllAirports(): Flow<List<AirportEntity>>
}
