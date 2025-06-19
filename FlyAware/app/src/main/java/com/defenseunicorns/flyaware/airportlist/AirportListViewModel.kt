package com.defenseunicorns.flyaware.airportlist

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AirportListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AirportListState())
    val state = _state.asStateFlow()

    fun onUiAction(action: UiAction) {
        when (action) {
            is UiAction.Add -> addAirport(action.id)
        }
    }

    private fun addAirport(id: String) {
        // TODO search for airport data

        Log.i("tag", "Added $id")
    }
}