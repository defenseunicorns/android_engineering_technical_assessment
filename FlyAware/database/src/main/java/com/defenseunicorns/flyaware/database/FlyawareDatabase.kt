package com.defenseunicorns.flyaware.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AirportDto::class,
    ],
    version = 1,

)
abstract class FlyawareDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
}
