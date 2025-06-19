package com.defenseunicorns.flyaware.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AirportDto::class,
        MetarDto::class,
    ],
    version = 2
)
abstract class FlyawareDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun metarDao(): MetarDao
}
