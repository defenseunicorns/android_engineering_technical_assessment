package com.defenseunicorns.flyaware.airportdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.AirportDetailsRoute
import com.defenseunicorns.flyaware.core.model.AirportMetar
import com.defenseunicorns.flyaware.data.ObserveAirportsUseCase
import com.defenseunicorns.flyaware.data.ObserveMetarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirportDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeAirports: ObserveAirportsUseCase,
    private val observeMetars: ObserveMetarsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<AirportMetar?>(null)
    val state = _state.asStateFlow()

    init {
        val id = AirportDetailsRoute.from(savedStateHandle).id

        viewModelScope.launch {
            // This should be handled at the database layer. This is a quick & dirty solution.
            combine(
                observeAirports(),
                observeMetars()
            ) { airports, metars ->
                airports.firstOrNull { it.icaoCode == id }?.let {
                    val metarsById = metars.associateBy { it.icaoCode }
                    AirportMetar(it, metarsById[it.icaoCode])
                }
            }.collect { latest ->
                _state.update { latest }
            }
        }
    }
}