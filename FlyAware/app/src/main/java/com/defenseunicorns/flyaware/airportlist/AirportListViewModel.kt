package com.defenseunicorns.flyaware.airportlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AirportListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AirportListState())
    val state = _state.asStateFlow()
}