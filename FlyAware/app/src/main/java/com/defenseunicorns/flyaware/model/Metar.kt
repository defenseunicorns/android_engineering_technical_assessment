/*
 * Copyright 2024 Defense Unicorns
 * SPDX-License-Identifier: LicenseRef-Defense-Unicorns-Commercial
 */
package com.defenseunicorns.flyaware.model

import java.time.*

/**
 * Domain model for a METAR (Meteorological Aerodrome Report)
 */
data class Metar(
    val icaoCode: String,
    val rawText: String,
    val observationTime: ZonedDateTime,
    val temperature: Double?, // Celsius
    val dewpoint: Double?, // Celsius
    val windDirection: Int?, // Degrees
    val windSpeed: Int?, // Knots
    val windGust: Int?, // Knots
    val visibility: Double?, // Statute Miles
    val altimeter: Double?, // InHg
    val flightCategory: FlightCategory,
    val cloudLayers: List<CloudLayer> = emptyList(),
    val weatherConditions: List<WeatherCondition> = emptyList()
) {
    companion object {
        val EMPTY = Metar(
            icaoCode = "",
            rawText = "",
            observationTime = ZonedDateTime.now(),
            temperature = null,
            dewpoint = null,
            windDirection = null,
            windSpeed = null,
            windGust = null,
            visibility = null,
            altimeter = null,
            flightCategory = FlightCategory.UNKNOWN
        )
    }
}

/**
 * Cloud layer information within a METAR or TAF
 */
data class CloudLayer(
    val coverage: CloudCoverage,
    val baseAltitudeFeet: Int?,
    val cloudType: String? = null
)

/**
 * Weather condition reported in a METAR or TAF
 */
data class WeatherCondition(
    val intensity: WeatherIntensity? = null,
    val descriptor: String? = null,
    val phenomena: List<String> = emptyList()
)

/**
 * Cloud coverage types
 */
enum class CloudCoverage {
    SKC, // Sky Clear
    CLR, // Clear below 12,000 ft
    FEW, // Few (1/8 to 2/8)
    SCT, // Scattered (3/8 to 4/8)
    BKN, // Broken (5/8 to 7/8)
    OVC, // Overcast (8/8)
    VV,  // Vertical Visibility (Obscured)
    UNKNOWN
}

/**
 * Weather intensity indications
 */
enum class WeatherIntensity {
    LIGHT,
    MODERATE, // Default intensity, usually not explicitly indicated
    HEAVY,
    VICINITY,
    UNKNOWN
}
