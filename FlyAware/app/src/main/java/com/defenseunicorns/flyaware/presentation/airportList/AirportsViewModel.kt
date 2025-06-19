package com.defenseunicorns.flyaware.presentation.airportList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.domain.usecase.FlyAwareUseCases
import com.defenseunicorns.flyaware.model.Metar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val useCases: FlyAwareUseCases
) : ViewModel() {

    private val _metars = MutableStateFlow<List<Metar>>(emptyList())
    val metars: StateFlow<List<Metar>> = _metars.asStateFlow()

    private val _savedAirports = MutableStateFlow<List<AirportEntity>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        observeSavedAirports()
    }

    private fun observeSavedAirports() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getAirports.invoke()
                .collect { airports ->
                    _savedAirports.value = airports
                    // todo only fetch one metar if the user just added a new airport?
                    fetchMetars()
                }
        }
    }

    private fun fetchMetars() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null

            try {
                val metarList = useCases.getMetars(_savedAirports.value.map { it.icaoCode })
                _metars.value = metarList
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}