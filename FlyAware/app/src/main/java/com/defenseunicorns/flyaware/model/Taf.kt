/*
 * Copyright 2024 Defense Unicorns
 * SPDX-License-Identifier: LicenseRef-Defense-Unicorns-Commercial
 */
package com.defenseunicorns.flyaware.model

import java.time.*

/**
 * Domain model for a TAF (Terminal Aerodrome Forecast)
 */
data class Taf(
    val icaoCode: String,
    val rawText: String,
    val issueTime: ZonedDateTime,
    val validFromTime: ZonedDateTime,
    val validUntilTime: ZonedDateTime,
    val forecastPeriods: List<ForecastPeriod> = emptyList()
) {
    companion object {
        val EMPTY = Taf(
            icaoCode = "",
            rawText = "",
            issueTime = ZonedDateTime.now(),
            validFromTime = ZonedDateTime.now(),
            validUntilTime = ZonedDateTime.now(),
            forecastPeriods = emptyList()
        )
    }
}

/**
 * Represents a forecast period within a TAF
 */
data class ForecastPeriod(
    val fromTime: ZonedDateTime,
    val untilTime: ZonedDateTime,
    val windDirection: Int?, // Degrees
    val windSpeed: Int?, // Knots
    val windGust: Int?, // Knots
    val visibility: Double?, // Statute Miles
    val flightCategory: FlightCategory,
    val cloudLayers: List<CloudLayer> = emptyList(),
    val weatherConditions: List<WeatherCondition> = emptyList(),
    val changeIndicator: ChangeIndicator = ChangeIndicator.NONE
)

/**
 * TAF change indicators
 */
enum class ChangeIndicator {
    NONE,   // Base forecast
    FM,     // From (rapid change)
    BECMG,  // Becoming (gradual change)
    TEMPO,  // Temporary
    PROB30, // 30% probability
    PROB40, // 40% probability
    TEMPO_PROB30, // Temporary with 30% probability
    TEMPO_PROB40  // Temporary with 40% probability
}
