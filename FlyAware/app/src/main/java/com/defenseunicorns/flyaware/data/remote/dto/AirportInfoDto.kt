package com.defenseunicorns.flyaware.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object for airport information from the aviation weather API.
 */
@Serializable
data class AirportInfoDto(
    @SerialName("icaoId")
    val icaoCode: String?,
    
    @SerialName("name")
    val name: String,

    @SerialName("state")
    val state: String? = null,
    
    @SerialName("country")
    val country: String? = null,
    
    @SerialName("elevation")
    val elevation: Int? = null,
    
    @SerialName("latitude")
    val latitude: Double? = null,
    
    @SerialName("longitude")
    val longitude: Double? = null
) 