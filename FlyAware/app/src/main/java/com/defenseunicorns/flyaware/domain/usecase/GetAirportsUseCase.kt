package com.defenseunicorns.flyaware.domain.usecase

import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAirportsUseCase @Inject constructor(
    private val repository: FlyAwareRepository
) {
    suspend operator fun invoke() : Flow<List<AirportEntity>> {
        return repository.getSavedAirports()
    }
}
