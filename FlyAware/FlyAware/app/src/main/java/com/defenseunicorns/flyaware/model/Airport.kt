/*
 * Copyright 2024 Defense Unicorns
 * SPDX-License-Identifier: LicenseRef-Defense-Unicorns-Commercial
 */
package com.defenseunicorns.flyaware.model

/**
 * Flight rules categories based on weather conditions
 */
enum class FlightCategory {
    VFR, // Visual Flight Rules
    MVFR, // Marginal Visual Flight Rules
    IFR, // Instrument Flight Rules
    LIFR, // Low Instrument Flight Rules
    UNKNOWN // When data is unavailable or cannot be determined
}

/**
 * Core domain model for an Airport
 */
data class Airport(
    val icaoCode: String,
    val iataCode: String? = null,
    val name: String,
    val city: String? = null,
    val state: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val elevation: Int? = null,
    val isFavorite: Boolean = false
) {
    companion object {
        val EMPTY = Airport(
            icaoCode = "",
            name = ""
        )
    }
}
