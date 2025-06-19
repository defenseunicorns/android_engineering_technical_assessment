package com.defenseunicorns.flyaware.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.defenseunicorns.flyaware.database.AirportDto.Companion.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(airport: AirportDto)

    @Delete
    suspend fun delete(airport: AirportDto)

    @Query("SELECT * FROM $TABLE")
    fun allAirports(): Flow<List<AirportDto>>
}
