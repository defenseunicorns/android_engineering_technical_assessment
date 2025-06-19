package com.defenseunicorns.flyaware.data

import com.defenseunicorns.flyaware.database.AirportDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAirportsUseCase @Inject constructor(
    private val airportDao: AirportDao
) {
    operator fun invoke() = airportDao.allAirports()
        .map { it.map { airport -> airport.asCoreModel() } }
}
