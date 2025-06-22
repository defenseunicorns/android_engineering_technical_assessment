package com.defenseunicorns.flyaware.presentation.airportDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.data.local.AirportEntity
import com.defenseunicorns.flyaware.domain.usecase.FlyAwareUseCases
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.Taf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirportDetailsViewModel @Inject constructor(
    private val useCases: FlyAwareUseCases
) : ViewModel() {

    private val _metar = MutableStateFlow<Metar?>(null)
    val metar: StateFlow<Metar?> = _metar.asStateFlow()

    private val _taf = MutableStateFlow<Taf?>(null)
    val taf: StateFlow<Taf?> = _taf.asStateFlow()

    private val _airport = MutableStateFlow<AirportEntity?>(null)
    val airport: StateFlow<AirportEntity?> = _airport.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _tafError = MutableStateFlow<String?>(null)
    val tafError: StateFlow<String?> = _tafError.asStateFlow()

    fun loadAirportData(icaoCode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _error.value = null
            _tafError.value = null

            // Load airport information first
            try {
                val airportInfo = useCases.getAirportByIcao(icaoCode)
                _airport.value = airportInfo
                Log.d("AirportDetailsViewModel", "Loaded airport info: ${airportInfo?.name}")
            } catch (e: Exception) {
                Log.e("AirportDetailsViewModel", "Failed to load airport info: ${e.message}")
                // Don't treat this as a fatal error since we can still show weather data
            }

            // Load METAR data first
            try {
                val metars = useCases.getMetars(listOf(icaoCode))
                _metar.value = metars.firstOrNull()
            } catch (e: Exception) {
                _error.value = "Failed to load METAR data: ${e.message}"
            }

            // Load TAF data separately - don't treat empty results as errors
            try {
                val tafs = useCases.getTafs(listOf(icaoCode))
                _taf.value = tafs.firstOrNull()

                // If no TAFs are returned, that's normal for some airports
                if (tafs.isEmpty()) {
                    Log.d(
                        "AirportDetailsViewModel",
                        "No TAFs available for $icaoCode - this is normal"
                    )
                }
            } catch (e: Exception) {
                _tafError.value = "Failed to load TAF data: ${e.message}"
                Log.e("AirportDetailsViewModel", "TAF loading error: ${e.message}")
            }

            _isLoading.value = false
        }
    }
} 