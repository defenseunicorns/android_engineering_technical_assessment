package com.defenseunicorns.flyaware.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airports")
data class AirportEntity(
    @PrimaryKey val icaoCode: String,
    val name: String? = null
)
