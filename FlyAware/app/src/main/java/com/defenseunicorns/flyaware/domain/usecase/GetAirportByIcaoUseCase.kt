package com.defenseunicorns.flyaware.domain.usecase

import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import javax.inject.Inject

class GetAirportByIcaoUseCase @Inject constructor(
    private val repository: FlyAwareRepository
) {
    suspend operator fun invoke(icaoCode: String): AirportEntity? {
        return repository.getAirportByIcao(icaoCode)
    }
} 