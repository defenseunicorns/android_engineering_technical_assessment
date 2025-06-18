package com.defenseunicorns.flyaware.presentation.airportList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // TODO Hardcoded for now
    private val airportCodes = listOf("KSFO", "KLAX", "KJFK")

    init {
        fetchMetars()
    }

    private fun fetchMetars() {
        viewModelScope.launch(Dispatchers.IO) {
            println("Fetching metars")
            _isLoading.value = true
            _error.value = null

            try {
                val metarList = useCases.getMetars(airportCodes)
                println("Metars: $metarList")
                _metars.value = metarList
            } catch (e: Exception) {
                println("Error fetching metars: $e")
                _error.value = e.message ?: "Unknown error"
            } finally {
                println("Finished fetching metars")
                _isLoading.value = false
            }
        }
    }
}