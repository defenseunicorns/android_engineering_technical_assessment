package com.defenseunicorns.flyaware.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.defenseunicorns.flyaware.core.model.FlightCategory
import com.defenseunicorns.flyaware.core.model.Metar
import com.defenseunicorns.flyaware.database.MetarDto.Companion.TABLE
import java.time.ZonedDateTime

@Entity(
    tableName = TABLE
)
data class MetarDto(
    @PrimaryKey
    @ColumnInfo(name = ICAO)
    val icaoCode: String,
    @ColumnInfo(name = RAW)
    val rawText: String = "",
    @ColumnInfo(name = OBS_TIME)
    val observationTime: String,
    @ColumnInfo(name = TEMP)
    val temperature: Double? = null,
    @ColumnInfo(name = DEW)
    val dewpoint: Double? = null,
    @ColumnInfo(name = WIND_DIR)
    val windDirection: Int? = null,
    @ColumnInfo(name = WIND_SPD)
    val windSpeed: Int? = null,
    @ColumnInfo(name = WIND_GUST)
    val windGust: Int? = null,
    @ColumnInfo(name = VIS)
    val visibility: String? = null,
    @ColumnInfo(name = ALTIM)
    val altimeter: Double? = null
) {
    fun asCoreModel() = Metar(
        icaoCode = icaoCode,
        rawText = rawText,
        observationTime = ZonedDateTime.parse(observationTime),
        temperature = temperature,
        dewpoint = dewpoint,
        windDirection = windDirection,
        windSpeed = windSpeed,
        windGust = windGust,
        visibility = visibility,
        altimeter = altimeter,
        flightCategory = FlightCategory.UNKNOWN, // TODO
        cloudLayers = emptyList(), // TODO
        weatherConditions = emptyList() // TODO
    )

    companion object {
        const val TABLE = "metar"
        const val ICAO = "icao"
        const val RAW = "raw"
        const val OBS_TIME = "obs_time"
        const val TEMP = "temp"
        const val DEW = "dew"
        const val WIND_DIR = "wind_dir"
        const val WIND_SPD = "wind_spd"
        const val WIND_GUST = "wind_gust"
        const val VIS = "vis"
        const val ALTIM = "altim"

        fun from(core: Metar) = MetarDto(
            icaoCode = core.icaoCode,
            rawText = core.rawText,
            observationTime = core.observationTime.toString(),
            temperature = core.temperature,
            dewpoint = core.dewpoint,
            windDirection = core.windDirection,
            windSpeed = core.windSpeed,
            windGust = core.windGust,
            visibility = core.visibility,
            altimeter = core.altimeter,
            // TODO add other fields.
        )
    }
}