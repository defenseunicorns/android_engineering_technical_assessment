package com.defenseunicorns.flyaware.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.defenseunicorns.flyaware.database.MetarDto.Companion.TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MetarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metar: MetarDto)

    @Delete
    suspend fun delete(metar: MetarDto)

    @Query("SELECT * FROM $TABLE")
    fun allMetars(): Flow<List<MetarDto>>
}
