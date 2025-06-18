package com.defenseunicorns.flyaware.domain.repository

import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.Taf


interface FlyAwareRepository {

    /**
     * Get the latest decoded METAR reports for the given ICAO airport codes.
     */
    suspend fun getMetars(icaoCodes: List<String>): List<Metar>
}
