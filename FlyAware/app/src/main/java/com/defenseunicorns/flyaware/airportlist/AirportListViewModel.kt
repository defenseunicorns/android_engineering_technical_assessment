package com.defenseunicorns.flyaware.airportlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.data.FetchAirportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirportListViewModel @Inject constructor(
    private val fetchAirport: FetchAirportUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AirportListState())
    val state = _state.asStateFlow()

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