package com.defenseunicorns.flyaware.network.model

import com.defenseunicorns.flyaware.core.model.Airport
import kotlinx.serialization.Serializable

@Serializable
data class AirportResponse(
    val id: String,
    val icaoId: String,
    val iataId: String,
    val name: String,
    val state: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val elev: Int
) {
    fun asCoreModel() = Airport(
        icaoCode = icaoId,
        iataCode = iataId,
        name = name,
        city = null,
        state = state,
        latitude = lat,
        longitude = lon,
        elevation = elev
    )
}
