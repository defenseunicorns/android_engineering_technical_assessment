package com.defenseunicorns.flyaware.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AirportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FlyAwareDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
}
