package com.defenseunicorns.flyaware.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AviationRepository @Inject constructor(
    private val observeAirports: ObserveAirportsUseCase,
    private val fetchMetar: FetchMetarUseCase
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun initialize() {
        scope.launch {
            observeAirports().collectLatest { airports ->
                airports.forEach {
                    fetchMetar(it.icaoCode)
                }
            }
        }
    }
}