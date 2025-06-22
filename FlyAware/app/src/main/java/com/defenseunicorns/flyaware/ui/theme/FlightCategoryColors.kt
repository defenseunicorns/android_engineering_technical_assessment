package com.defenseunicorns.flyaware.ui.theme

import androidx.compose.ui.graphics.Color
import com.defenseunicorns.flyaware.model.FlightCategory

object FlightCategoryColors {
    fun getColor(flightCategory: FlightCategory): Color {
        return when (flightCategory) {
            FlightCategory.VFR -> VfrColor
            FlightCategory.MVFR -> MvfrColor
            FlightCategory.IFR -> IfrColor
            FlightCategory.LIFR -> LifrColor
            FlightCategory.UNKNOWN -> UnknownColor
        }
    }

    fun getBackgroundColor(flightCategory: FlightCategory): Color {
        return when (flightCategory) {
            FlightCategory.VFR -> VfrColor.copy(alpha = 0.1f)
            FlightCategory.MVFR -> MvfrColor.copy(alpha = 0.1f)
            FlightCategory.IFR -> IfrColor.copy(alpha = 0.1f)
            FlightCategory.LIFR -> LifrColor.copy(alpha = 0.1f)
            FlightCategory.UNKNOWN -> UnknownColor.copy(alpha = 0.1f)
        }
    }

    fun getDisplayName(flightCategory: FlightCategory): String {
        return when (flightCategory) {
            FlightCategory.VFR -> "VFR"
            FlightCategory.MVFR -> "MVFR"
            FlightCategory.IFR -> "IFR"
            FlightCategory.LIFR -> "LIFR"
            FlightCategory.UNKNOWN -> "UNK"
        }
    }
} 