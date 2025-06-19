package com.defenseunicorns.flyaware.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.defenseunicorns.flyaware.core.model.Airport
import com.defenseunicorns.flyaware.database.AirportDto.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class AirportDto(
    @PrimaryKey
    @ColumnInfo(name = ICAO)
    val icaoCode: String,
    @ColumnInfo(name = IATA)
    val iataCode: String? = null,
    @ColumnInfo(name = NAME)
    val name: String,
    @ColumnInfo(name = CITY)
    val city: String? = null,
    @ColumnInfo(name = STATE)
    val state: String? = null,
    @ColumnInfo(name = LATITUDE)
    val latitude: Double? = null,
    @ColumnInfo(name = LONGITUDE)
    val longitude: Double? = null,
    @ColumnInfo(name = ELEVATION)
    val elevation: Int? = null,
    @ColumnInfo(name = FAVORITE)
    val isFavorite: Boolean = false
) {
    fun asCoreModel() = Airport(
        icaoCode = icaoCode,
        iataCode = iataCode,
        name = name,
        city = city,
        state = state,
        latitude = latitude,
        longitude = longitude,
        elevation = elevation,
        isFavorite = isFavorite
    )

    companion object {
        const val TABLE = "airport"
        const val ICAO = "icao"
        const val IATA = "iata"
        const val NAME = "name"
        const val CITY = "city"
        const val STATE = "state"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ELEVATION = "elevation"
        const val FAVORITE = "favorite"

        fun from(core: Airport) = AirportDto(
            icaoCode = core.icaoCode,
            iataCode = core.iataCode,
            name = core.name,
            city = core.city,
            state = core.state,
            latitude = core.latitude,
            longitude = core.longitude,
            elevation = core.elevation,
            isFavorite = core.isFavorite
        )
    }
}