package com.defenseunicorns.flyaware.data.remote.dto

import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.CloudLayer
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.presentation.utils.FlightCategoryCalculator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Serializable
data class MetarDto(
    @SerialName("icaoId") val icaoId: String?,
    @SerialName("rawOb") val rawText: String?,
    @SerialName("obsTime") val observationTime: Long?,
    @SerialName("temp") val temperature: Double?,
    @SerialName("dewp") val dewpoint: Double?,
    @SerialName("wdir") val windDirection: Int?,
    @SerialName("wspd") val windSpeed: Int?,
    @SerialName("wgst") val windGust: Int?,
    @SerialName("visib") val visibility: String?,
    @SerialName("altim") val altimeter: Double?,
    @SerialName("clouds") val clouds: List<MetarCloudDto>?,
    @SerialName("wxString") val wxString: String? = null,
    @SerialName("lat") val latitude: Double? = null,
    @SerialName("lon") val longitude: Double? = null
) {
    fun toMetar(): Metar {
        val obsTime = try {
            observationTime?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneOffset.UTC) } ?: ZonedDateTime.now()
        } catch (e: Exception) {
            ZonedDateTime.now()
        }

        val cloudLayers = clouds?.mapNotNull { it.toCloudLayer() } ?: emptyList()
        
        // Parse visibility, removing "+" and handling non-numeric values
        val parsedVisibility = visibility?.replace("+", "")?.toDoubleOrNull()

        val flightCategory = FlightCategoryCalculator.calculateFlightCategory(
            visibility = parsedVisibility,
            cloudLayers = cloudLayers
        )

        return Metar(
            icaoCode = icaoId ?: "",
            rawText = rawText ?: "",
            observationTime = obsTime,
            temperature = temperature,
            dewpoint = dewpoint,
            windDirection = windDirection,
            windSpeed = windSpeed,
            windGust = windGust,
            visibility = parsedVisibility,
            altimeter = altimeter,
            flightCategory = flightCategory,
            cloudLayers = cloudLayers,
            weatherConditions = parseWeatherConditions(wxString, rawText),
            latitude = latitude,
            longitude = longitude
        )
    }
    
    private fun parseWeatherConditions(wxString: String?, rawText: String?): List<com.defenseunicorns.flyaware.model.WeatherCondition> {
        // First try to use wxString if available
        if (!wxString.isNullOrBlank()) {
            return parseWeatherString(wxString)
        }
        
        // If wxString is not available, try to extract weather from raw METAR text
        if (!rawText.isNullOrBlank()) {
            return parseWeatherFromRawMetar(rawText)
        }
        
        return emptyList()
    }
    
    private fun parseWeatherString(wxString: String): List<com.defenseunicorns.flyaware.model.WeatherCondition> {
        if (wxString.isBlank()) return emptyList()
        
        val conditions = mutableListOf<com.defenseunicorns.flyaware.model.WeatherCondition>()
        
        // Parse intensity
        val intensity = when {
            wxString.contains("+") -> com.defenseunicorns.flyaware.model.WeatherIntensity.HEAVY
            wxString.contains("-") -> com.defenseunicorns.flyaware.model.WeatherIntensity.LIGHT
            wxString.contains("VC") -> com.defenseunicorns.flyaware.model.WeatherIntensity.VICINITY
            else -> com.defenseunicorns.flyaware.model.WeatherIntensity.MODERATE
        }
        
        val phenomena = mutableListOf<String>()
        
        // Parse weather phenomena - check for each phenomenon separately since they can be combined
        if (wxString.contains("RA")) phenomena.add("Rain")
        if (wxString.contains("SN")) phenomena.add("Snow")
        if (wxString.contains("DZ")) phenomena.add("Drizzle")
        if (wxString.contains("SG")) phenomena.add("Snow Grains")
        if (wxString.contains("IC")) phenomena.add("Ice Crystals")
        if (wxString.contains("PL")) phenomena.add("Ice Pellets")
        if (wxString.contains("GR")) phenomena.add("Hail")
        if (wxString.contains("GS")) phenomena.add("Small Hail")
        if (wxString.contains("UP")) phenomena.add("Unknown Precipitation")
        if (wxString.contains("BR")) phenomena.add("Mist")
        if (wxString.contains("FG")) phenomena.add("Fog")
        if (wxString.contains("FU")) phenomena.add("Smoke")
        if (wxString.contains("VA")) phenomena.add("Volcanic Ash")
        if (wxString.contains("DU")) phenomena.add("Widespread Dust")
        if (wxString.contains("SA")) phenomena.add("Sand")
        if (wxString.contains("HZ")) phenomena.add("Haze")
        if (wxString.contains("PY")) phenomena.add("Spray")
        if (wxString.contains("PO")) phenomena.add("Dust/Sand Whirls")
        if (wxString.contains("SQ")) phenomena.add("Squalls")
        if (wxString.contains("FC")) phenomena.add("Funnel Cloud")
        if (wxString.contains("SS")) phenomena.add("Sandstorm")
        if (wxString.contains("DS")) phenomena.add("Duststorm")
        if (wxString.contains("TS")) phenomena.add("Thunderstorm")
        if (wxString.contains("SH")) phenomena.add("Shower")
        if (wxString.contains("FZ")) phenomena.add("Freezing")
        if (wxString.contains("MI")) phenomena.add("Shallow")
        if (wxString.contains("BC")) phenomena.add("Patches")
        if (wxString.contains("DR")) phenomena.add("Low Drifting")
        if (wxString.contains("BL")) phenomena.add("Blowing")
        if (wxString.contains("PR")) phenomena.add("Partial")
        
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
    
    private fun parseWeatherFromRawMetar(rawText: String): List<com.defenseunicorns.flyaware.model.WeatherCondition> {
        val conditions = mutableListOf<com.defenseunicorns.flyaware.model.WeatherCondition>()
        
        // Split the raw METAR into parts and look for weather phenomena
        val parts = rawText.split(" ")
        
        for (part in parts) {
            // Look for weather phenomena patterns in METAR format
            // Weather phenomena typically appear between visibility and cloud information
            // Also check remarks section for weather ending/beginning indicators
            if (isWeatherPhenomenon(part)) {
                val intensity = parseIntensity(part)
                val phenomena = parsePhenomena(part)
                
                if (phenomena.isNotEmpty()) {
                    conditions.add(
                        com.defenseunicorns.flyaware.model.WeatherCondition(
                            intensity = intensity,
                            descriptor = null,
                            phenomena = phenomena
                        )
                    )
                }
            }
        }
        
        return conditions
    }
    
    private fun isWeatherPhenomenon(part: String): Boolean {
        // Check if the part contains weather phenomena codes
        // Remove intensity indicators for checking, but handle VC specially
        val cleanPart = part.removePrefix("+").removePrefix("-")
        
        val weatherCodes = listOf("RA", "SN", "DZ", "SG", "IC", "PL", "GR", "GS", "UP", 
                                 "BR", "FG", "FU", "VA", "DU", "SA", "HZ", "PY", "PO", 
                                 "SQ", "FC", "SS", "DS", "TS", "SH", "FZ", "MI", "BC", 
                                 "DR", "BL", "PR")
        
        // Check if the cleaned part contains any weather phenomena
        if (weatherCodes.any { cleanPart.contains(it) }) {
            return true
        }
        
        // Special case: if the part starts with "VC" and contains weather phenomena
        if (part.startsWith("VC")) {
            return weatherCodes.any { part.contains(it) }
        }
        
        return false
    }
    
    private fun parseIntensity(part: String): com.defenseunicorns.flyaware.model.WeatherIntensity {
        return when {
            part.startsWith("+") -> com.defenseunicorns.flyaware.model.WeatherIntensity.HEAVY
            part.startsWith("-") -> com.defenseunicorns.flyaware.model.WeatherIntensity.LIGHT
            part.contains("VC") -> com.defenseunicorns.flyaware.model.WeatherIntensity.VICINITY
            else -> com.defenseunicorns.flyaware.model.WeatherIntensity.MODERATE
        }
    }
    
    private fun parsePhenomena(part: String): List<String> {
        val phenomena = mutableListOf<String>()
        
        // Remove intensity indicators for parsing, but handle VC specially
        val cleanPart = part.removePrefix("+").removePrefix("-")
        
        // Determine which part to check for phenomena
        val partToCheck = if (part.startsWith("VC")) part else cleanPart
        
        // Check for each weather phenomenon in the part to check
        // Note: We need to check for each phenomenon separately since they can be combined
        if (partToCheck.contains("RA")) phenomena.add("Rain")
        if (partToCheck.contains("SN")) phenomena.add("Snow")
        if (partToCheck.contains("DZ")) phenomena.add("Drizzle")
        if (partToCheck.contains("SG")) phenomena.add("Snow Grains")
        if (partToCheck.contains("IC")) phenomena.add("Ice Crystals")
        if (partToCheck.contains("PL")) phenomena.add("Ice Pellets")
        if (partToCheck.contains("GR")) phenomena.add("Hail")
        if (partToCheck.contains("GS")) phenomena.add("Small Hail")
        if (partToCheck.contains("UP")) phenomena.add("Unknown Precipitation")
        if (partToCheck.contains("BR")) phenomena.add("Mist")
        if (partToCheck.contains("FG")) phenomena.add("Fog")
        if (partToCheck.contains("FU")) phenomena.add("Smoke")
        if (partToCheck.contains("VA")) phenomena.add("Volcanic Ash")
        if (partToCheck.contains("DU")) phenomena.add("Widespread Dust")
        if (partToCheck.contains("SA")) phenomena.add("Sand")
        if (partToCheck.contains("HZ")) phenomena.add("Haze")
        if (partToCheck.contains("PY")) phenomena.add("Spray")
        if (partToCheck.contains("PO")) phenomena.add("Dust/Sand Whirls")
        if (partToCheck.contains("SQ")) phenomena.add("Squalls")
        if (partToCheck.contains("FC")) phenomena.add("Funnel Cloud")
        if (partToCheck.contains("SS")) phenomena.add("Sandstorm")
        if (partToCheck.contains("DS")) phenomena.add("Duststorm")
        if (partToCheck.contains("TS")) phenomena.add("Thunderstorm")
        if (partToCheck.contains("SH")) phenomena.add("Shower")
        if (partToCheck.contains("FZ")) phenomena.add("Freezing")
        if (partToCheck.contains("MI")) phenomena.add("Shallow")
        if (partToCheck.contains("BC")) phenomena.add("Patches")
        if (partToCheck.contains("DR")) phenomena.add("Low Drifting")
        if (partToCheck.contains("BL")) phenomena.add("Blowing")
        if (partToCheck.contains("PR")) phenomena.add("Partial")
        
        return phenomena
    }
}

@Serializable
data class MetarCloudDto(
    @SerialName("cover") val cover: String?,
    @SerialName("base") val base: Int?
) {
    fun toCloudLayer(): CloudLayer? {
        val coverage = when (cover) {
            "CLR" -> CloudCoverage.CLR
            "SKC" -> CloudCoverage.SKC
            "FEW" -> CloudCoverage.FEW
            "SCT" -> CloudCoverage.SCT
            "BKN" -> CloudCoverage.BKN
            "OVC" -> CloudCoverage.OVC
            "VV" -> CloudCoverage.VV
            else -> return null // Ignore unknown coverage types
        }
        return CloudLayer(
            coverage = coverage,
            baseAltitudeFeet = base,
            cloudType = null // Not typically provided in METAR JSON
        )
    }
}