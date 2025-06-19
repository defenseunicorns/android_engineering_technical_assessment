package com.defenseunicorns.flyaware

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object AirportListRoute

@Serializable
data class AirportDetailsRoute(val id: String) {
    companion object {
        fun from(handle: SavedStateHandle) = handle.toRoute<AirportDetailsRoute>()
    }
}