package com.defenseunicorns.flyaware.data.remote.dto

import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.CloudLayer
import com.defenseunicorns.flyaware.model.FlightCategory
import com.defenseunicorns.flyaware.model.Metar
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class MetarDto(
    @SerialName("metar_id") val metarId: Int? = null,
    @SerialName("icaoId") val icaoId: String? = null,
    @SerialName("receiptTime") val receiptTime: String? = null,
    @SerialName("obsTime") val obsTime: Int? = null,
    @SerialName("reportTime") val reportTime: String? = null,
    @SerialName("temp") val temp: Double? = null,
    @SerialName("dewp") val dewp: Double? = null,
    @SerialName("wdir") val windDir: Int? = null,
    @SerialName("wspd") val windSpeed: Int? = null,
    @SerialName("wgst") val windGust: Int? = null,
    @SerialName("visib") val visibility: String? = null,
    @SerialName("altim") val altimeter: Double? = null,
    @SerialName("slp") val seaLevelPressure: Double? = null,
    @SerialName("qcField") val qcField: Int? = null,
    @SerialName("wxString") val weather: String? = null,
    @SerialName("presTend") val pressureTendency: String? = null,
    @SerialName("maxT") val maxTemp: Double? = null,
    @SerialName("minT") val minTemp: Double? = null,
    @SerialName("maxT24") val maxTemp24: Double? = null,
    @SerialName("minT24") val minTemp24: Double? = null,
    @SerialName("precip") val precip: Double? = null,
    @SerialName("pcp3hr") val precip3hr: Double? = null,
    @SerialName("pcp6hr") val precip6hr: Double? = null,
    @SerialName("pcp24hr") val precip24hr: Double? = null,
    @SerialName("snow") val snow: Double? = null,
    @SerialName("vertVis") val verticalVisibility: Int? = null,
    @SerialName("metarType") val metarType: String? = null,
    @SerialName("rawOb") val rawObservation: String? = null,
    @SerialName("mostRecent") val mostRecent: Int? = null,
    @SerialName("lat") val latitude: Double?  = null,
    @SerialName("lon") val longitude: Double? = null,
    @SerialName("elev") val elevation: Int? = null,
    @SerialName("prior") val prior: Int? = null,
    @SerialName("name") val stationName: String? = null,
    @SerialName("clouds") val clouds: List<CloudLayerDto>? = null
) {
    fun toMetar(): Metar {
        return Metar(
            icaoCode = icaoId.orEmpty(),
            rawText =rawObservation.orEmpty(),
            observationTime = ZonedDateTime.now(),//reportTime?.toZonedDateTime() ?: ZonedDateTime.now(),
            temperature = temp,
            dewpoint = dewp,
            windDirection = windDir,
            windSpeed = windSpeed,
            windGust = windGust,
            visibility = getVisiblity(visibility),
            altimeter = altimeter,
            flightCategory = FlightCategory.UNKNOWN,// todo doesn't seem to be part of a metar
            cloudLayers = clouds?.map {
                CloudLayer(baseAltitudeFeet = it.base, coverage = it.fromEnum())
            } ?: emptyList(),
            weatherConditions = emptyList()  // todo doesn't seem to be part of a metar
        )

    }
}

fun getVisiblity(visibility: String?): Double {
    return if (visibility?.endsWith('+') == true) {
        visibility.removeSuffix("+").toDoubleOrNull() ?: 0.0
    } else visibility?.toDoubleOrNull() ?: 0.0
}

@Serializable
data class CloudLayerDto(
    @SerialName("cover") val cover: String? = null,
    @SerialName("base") val base: Int? = null
) {
    fun fromEnum(): CloudCoverage {
        CloudCoverage.entries.forEach {
            if (it.name.equals(cover, true)) {
                // todo map the name to a human readable value instead of the code
                return it
            }
        }
        return CloudCoverage.UNKNOWN
    }
}

fun String.toZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.parse(this)

}