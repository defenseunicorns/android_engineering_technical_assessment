package com.defenseunicorns.flyaware.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airports")
data class AirportEntity(
    @PrimaryKey val icaoCode: String,
    val name: String? = null,
    val state: String? = null,
    val country: String? = null,
    val elevation: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
