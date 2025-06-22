package com.defenseunicorns.flyaware.data.remote.dto

import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.CloudLayer
import com.defenseunicorns.flyaware.model.ChangeIndicator
import com.defenseunicorns.flyaware.model.ForecastPeriod
import com.defenseunicorns.flyaware.model.Taf
import com.defenseunicorns.flyaware.presentation.utils.FlightCategoryCalculator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class TafDto(
    @SerialName("tafId") val tafId: Int? = null,
    @SerialName("icaoId") val icaoId: String? = null,
    @SerialName("dbPopTime") val dbPopTime: String? = null,
    @SerialName("bulletinTime") val bulletinTime: String? = null,
    @SerialName("issueTime") val issueTime: String? = null,
    @SerialName("validTimeFrom") val validTimeFrom: Long? = null,
    @SerialName("validTimeTo") val validTimeTo: Long? = null,
    @SerialName("rawTAF") val rawTAF: String? = null,
    @SerialName("mostRecent") val mostRecent: Int? = null,
    @SerialName("remarks") val remarks: String? = null,
    @SerialName("lat") val lat: Double? = null,
    @SerialName("lon") val lon: Double? = null,
    @SerialName("elev") val elev: Int? = null,
    @SerialName("prior") val prior: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("fcsts") val fcsts: List<TafForecastDto>? = null
) {
    fun toTaf() : Taf {
        val errorTime = ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(0), java.time.ZoneOffset.UTC)
        
        // Convert timestamps to ZonedDateTime
        val issueTime = try {
            this.issueTime?.toZonedDateTime() ?: errorTime
        } catch (e: Exception) {
            errorTime
        }
        
        val validFromTime = try {
            validTimeFrom?.let { timestamp ->
                ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp), java.time.ZoneOffset.UTC)
            } ?: errorTime
        } catch (e: Exception) {
            errorTime
        }
        
        val validUntilTime = try {
            validTimeTo?.let { timestamp ->
                ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp), java.time.ZoneOffset.UTC)
            } ?: errorTime.plusHours(24)
        } catch (e: Exception) {
            errorTime.plusHours(24)
        }
        
        // Convert forecast periods
        val forecastPeriods = fcsts?.mapNotNull { forecastDto ->
            try {
                forecastDto.toForecastPeriod(validFromTime)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
        
        return Taf(
            rawText = rawTAF ?: "",
            icaoCode = icaoId ?: "",
            issueTime = issueTime,
            validFromTime = validFromTime,
            validUntilTime = validUntilTime,
            forecastPeriods = forecastPeriods
        )
    }
}

@Serializable
data class TafForecastDto(
    @SerialName("timeGroup") val timeGroup: Int? = null,
    @SerialName("timeFrom") val timeFrom: Long? = null,
    @SerialName("timeTo") val timeTo: Long? = null,
    @SerialName("timeBec") val timeBec: Long? = null,
    @SerialName("fcstChange") val fcstChange: String? = null,
    @SerialName("probability") val probability: Int? = null,
    @SerialName("wdir") val wdir: String? = null,
    @SerialName("wspd") val wspd: Int? = null,
    @SerialName("wgst") val wgst: Int? = null,
    @SerialName("wshearHgt") val wshearHgt: Int? = null,
    @SerialName("wshearDir") val wshearDir: Int? = null,
    @SerialName("wshearSpd") val wshearSpd: Int? = null,
    @SerialName("visib") val visib: String? = null,
    @SerialName("altim") val altim: Double? = null,
    @SerialName("vertVis") val vertVis: Int? = null,
    @SerialName("wxString") val wxString: String? = null,
    @SerialName("notDecoded") val notDecoded: String? = null,
    @SerialName("clouds") val clouds: List<TafCloudDto>? = null,
    @SerialName("icgTurb") val icgTurb: List<TafIcingTurbulenceDto>? = null,
    @SerialName("temp") val temp: List<TafTemperatureDto>? = null
) {
    fun toForecastPeriod(baseTime: ZonedDateTime): ForecastPeriod {
        val errorTime = ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(0), java.time.ZoneOffset.UTC)
        
        val fromTime = try {
            timeFrom?.let { timestamp ->
                ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp), java.time.ZoneOffset.UTC)
            } ?: baseTime
        } catch (e: Exception) {
            errorTime
        }
        
        val untilTime = try {
            timeTo?.let { timestamp ->
                ZonedDateTime.ofInstant(java.time.Instant.ofEpochSecond(timestamp), java.time.ZoneOffset.UTC)
            } ?: baseTime.plusHours(6)
        } catch (e: Exception) {
            errorTime.plusHours(6)
        }
        
        val changeIndicator = when (fcstChange) {
            "FM" -> ChangeIndicator.FM
            "BECMG" -> ChangeIndicator.BECMG
            "TEMPO" -> ChangeIndicator.TEMPO
            "PROB30" -> ChangeIndicator.PROB30
            "PROB40" -> ChangeIndicator.PROB40
            else -> ChangeIndicator.NONE
        }
        
        val cloudLayers = clouds?.mapNotNull { cloudDto ->
            try {
                cloudDto.toCloudLayer()
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()
        
        // Parse wind direction - handle "VRB" case
        val windDirection = wdir?.let { dir ->
            if (dir == "VRB") null else dir.toIntOrNull()
        }
        
        // Parse visibility - handle "6+" case
        val visibility = visib?.let { vis ->
            if (vis == "6+") 6.0 else vis.toDoubleOrNull()
        }
        
        return ForecastPeriod(
            fromTime = fromTime,
            untilTime = untilTime,
            windDirection = windDirection,
            windSpeed = wspd,
            windGust = wgst,
            visibility = visibility,
            flightCategory = FlightCategoryCalculator.calculateFlightCategory(visibility, cloudLayers),
            cloudLayers = cloudLayers,
            weatherConditions = parseWeatherConditions(wxString),
            changeIndicator = changeIndicator
        )
    }
    
    private fun parseWeatherConditions(wxString: String?): List<com.defenseunicorns.flyaware.model.WeatherCondition> {
        if (wxString.isNullOrBlank()) return emptyList()
        
        val conditions = mutableListOf<com.defenseunicorns.flyaware.model.WeatherCondition>()
        
        // Simple parsing of common weather phenomena
        val intensity = when {
            wxString.contains("+") -> com.defenseunicorns.flyaware.model.WeatherIntensity.HEAVY
            wxString.contains("-") -> com.defenseunicorns.flyaware.model.WeatherIntensity.LIGHT
            wxString.contains("VC") -> com.defenseunicorns.flyaware.model.WeatherIntensity.VICINITY
            else -> com.defenseunicorns.flyaware.model.WeatherIntensity.MODERATE
        }
        
        val phenomena = mutableListOf<String>()
        
        // Common weather phenomena
        when {
            wxString.contains("RA") -> phenomena.add("Rain")
            wxString.contains("SN") -> phenomena.add("Snow")
            wxString.contains("DZ") -> phenomena.add("Drizzle")
            wxString.contains("SG") -> phenomena.add("Snow Grains")
            wxString.contains("IC") -> phenomena.add("Ice Crystals")
            wxString.contains("PL") -> phenomena.add("Ice Pellets")
            wxString.contains("GR") -> phenomena.add("Hail")
            wxString.contains("GS") -> phenomena.add("Small Hail")
            wxString.contains("UP") -> phenomena.add("Unknown Precipitation")
            wxString.contains("BR") -> phenomena.add("Mist")
            wxString.contains("FG") -> phenomena.add("Fog")
            wxString.contains("FU") -> phenomena.add("Smoke")
            wxString.contains("VA") -> phenomena.add("Volcanic Ash")
            wxString.contains("DU") -> phenomena.add("Widespread Dust")
            wxString.contains("SA") -> phenomena.add("Sand")
            wxString.contains("HZ") -> phenomena.add("Haze")
            wxString.contains("PY") -> phenomena.add("Spray")
            wxString.contains("PO") -> phenomena.add("Dust/Sand Whirls")
            wxString.contains("SQ") -> phenomena.add("Squalls")
            wxString.contains("FC") -> phenomena.add("Funnel Cloud")
            wxString.contains("SS") -> phenomena.add("Sandstorm")
            wxString.contains("DS") -> phenomena.add("Duststorm")
        }
        
        if (phenomena.isNotEmpty()) {
            conditions.add(
                com.defenseunicorns.flyaware.model.WeatherCondition(
                    intensity = intensity,
                    descriptor = null,
                    phenomena = phenomena
                )
            )
        }
        
        return conditions
    }
}

@Serializable
data class TafCloudDto(
    @SerialName("cover") val cover: String? = null,
    @SerialName("base") val base: Int? = null,
    @SerialName("type") val type: String? = null
) {
    fun toCloudLayer(): CloudLayer {
        val coverage = when (cover?.uppercase()) {
            "SKC" -> CloudCoverage.SKC
            "CLR" -> CloudCoverage.CLR
            "CAVOK" -> CloudCoverage.CAVOK
            "FEW" -> CloudCoverage.FEW
            "SCT" -> CloudCoverage.SCT
            "BKN" -> CloudCoverage.BKN
            "OVC" -> CloudCoverage.OVC
            "OVX" -> CloudCoverage.OVC // Treat OVX as OVC
            "VV" -> CloudCoverage.VV
            else -> CloudCoverage.UNKNOWN
        }
        
        return CloudLayer(
            coverage = coverage,
            baseAltitudeFeet = base,
            cloudType = type
        )
    }
}

@Serializable
data class TafIcingTurbulenceDto(
    @SerialName("var") val variable: String? = null, // "ICE" or "TURB"
    @SerialName("intensity") val intensity: Int? = null,
    @SerialName("minAlt") val minAlt: Int? = null,
    @SerialName("maxAlt") val maxAlt: Int? = null
)

@Serializable
data class TafTemperatureDto(
    @SerialName("validTime") val validTime: Long? = null,
    @SerialName("sfcTemp") val sfcTemp: Int? = null,
    @SerialName("maxOrMin") val maxOrMin: String? = null // "MAX" or "MIN"
)
