package com.defenseunicorns.flyaware.presentation.utils

import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.FlightCategory

object FlightCategoryCalculator {

    /**
     * Calculate flight category based on visibility and ceiling
     * VFR: Visibility > 5 miles and ceiling > 3,000 feet
     * MVFR: Visibility 3-5 miles and/or ceiling 1,000-3,000 feet
     * IFR: Visibility 1-3 miles and/or ceiling 500-1,000 feet
     * LIFR: Visibility < 1 mile and/or ceiling < 500 feet
     */
    fun calculateFlightCategory(
        visibility: Double?,
        cloudLayers: List<com.defenseunicorns.flyaware.model.CloudLayer>
    ): FlightCategory {
        // Find the lowest ceiling
        val lowestCeiling = cloudLayers
            .filter { it.coverage == CloudCoverage.BKN || it.coverage == CloudCoverage.OVC }
            .mapNotNull { it.baseAltitudeFeet }
            .minOrNull()

        return when {
            // LIFR conditions
            (visibility != null && visibility < 1.0) || (lowestCeiling != null && lowestCeiling < 500) -> {
                FlightCategory.LIFR
            }
            // IFR conditions
            (visibility != null && visibility < 3.0) || (lowestCeiling != null && lowestCeiling < 1000) -> {
                FlightCategory.IFR
            }
            // MVFR conditions
            (visibility != null && visibility < 5.0) || (lowestCeiling != null && lowestCeiling < 3000) -> {
                FlightCategory.MVFR
            }
            // VFR conditions (default)
            else -> {
                FlightCategory.VFR
            }
        }
    }
} 