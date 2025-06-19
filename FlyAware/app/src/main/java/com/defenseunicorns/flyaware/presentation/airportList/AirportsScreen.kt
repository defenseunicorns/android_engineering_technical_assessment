package com.defenseunicorns.flyaware.presentation.airportList

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.defenseunicorns.flyaware.model.Metar

@Composable
fun AirportsScreen(
    viewModel: AirportsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val metars by viewModel.metars.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            error != null -> {
                Text(
                    text = error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("METARs", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    metars.forEach { metar ->
                        MetarItem(metar = metar)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MetarItem(metar: Metar) {
    Column {
        Text("Station: ${metar.icaoCode}")
        Text("Flight Category: ${metar.flightCategory}")
        Text("Temperature: ${metar.temperature}°C")
        Text("Wind: ${metar.windSpeed ?: "--"} kt")
        Text("Visibility: ${metar.visibility ?: "--"} mi")
    }
}
