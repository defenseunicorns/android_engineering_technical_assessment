package com.defenseunicorns.flyaware.domain.usecase

import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.model.Metar
import javax.inject.Inject

class GetMetarsUseCase @Inject constructor(
    private val repository: FlyAwareRepository
) {
    suspend operator fun invoke(icaoCodes: List<String>): List<Metar> {
        return repository.getMetars(icaoCodes)
    }
}