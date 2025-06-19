package com.defenseunicorns.flyaware.domain.usecase

import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import javax.inject.Inject

class RemoveAirportUseCase @Inject constructor(
    private val repository: FlyAwareRepository
) {
    suspend operator fun invoke(icaoCode: String) {
        repository.removeAirport(icaoCode)
    }
}
