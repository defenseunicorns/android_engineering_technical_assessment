package com.defenseunicorns.flyaware.domain.usecase

import com.defenseunicorns.flyaware.domain.repository.FlyAwareRepository
import com.defenseunicorns.flyaware.model.Taf
import javax.inject.Inject

class GetTafsUseCase @Inject constructor(
    private val repository: FlyAwareRepository
) {
    suspend operator fun invoke(icaoCodes: List<String>): List<Taf> {
        return repository.getTafs(icaoCodes)
    }
} 