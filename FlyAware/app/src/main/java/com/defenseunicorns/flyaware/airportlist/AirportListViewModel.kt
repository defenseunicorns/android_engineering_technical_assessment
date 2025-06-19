package com.defenseunicorns.flyaware.airportlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.core.model.AirportMetar
import com.defenseunicorns.flyaware.data.FetchAirportUseCase
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
class AirportListViewModel @Inject constructor(
    private val fetchAirport: FetchAirportUseCase,
    private val observeAirports: ObserveAirportsUseCase,
    private val observeMetars: ObserveMetarsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<List<AirportMetar>>(emptyList())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // This would be more appropriately handled at the database layer with a join.
            combine(
                observeAirports(),
                observeMetars()
            ) { airports, metars ->
                val metarsById = metars.associateBy { it.icaoCode }

                airports.map {
                    AirportMetar(it, metarsById[it.icaoCode])
                }
            }.collect { latest ->
                _state.update { latest }
            }
        }
    }

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.Add -> addAirport(action.id)
        }
    }

    private fun addAirport(id: String) {
        viewModelScope.launch {
            fetchAirport(id)
            Log.i("tag", "Added $id")
        }
    }
}