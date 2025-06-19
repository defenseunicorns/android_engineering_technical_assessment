package com.defenseunicorns.flyaware

import kotlinx.serialization.Serializable

@Serializable
class AirportList

@Serializable
data class AirportDetails(val icaoId: String)