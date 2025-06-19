package com.defenseunicorns.flyaware.data

import com.defenseunicorns.flyaware.database.MetarDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMetarsUseCase @Inject constructor(
    private val metarDao: MetarDao
) {
    operator fun invoke() = metarDao.allMetars()
        .map { it.map { metar -> metar.asCoreModel() } }
}
