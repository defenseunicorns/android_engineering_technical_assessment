package com.defenseunicorns.flyaware.presentation.addAirportScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.defenseunicorns.flyaware.domain.usecase.AddAirportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAirportViewModel @Inject constructor(
    private val addAirportUseCase: AddAirportUseCase
) : ViewModel() {

    var inputIcao by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onInputChange(newInput: String) {
        inputIcao = newInput.uppercase()
        errorMessage = null
    }

    fun addAirport(onSuccess: () -> Unit) {
        if (inputIcao.length != 4) {
            errorMessage = "ICAO code must be 4 characters"
            return
        }

        viewModelScope.launch {
            addAirportUseCase(inputIcao)
            onSuccess()
            inputIcao = ""
        }
    }
}
