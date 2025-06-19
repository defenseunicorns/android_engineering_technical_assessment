package com.defenseunicorns.flyaware.presentation.airportList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.CloudLayer
import com.defenseunicorns.flyaware.model.FlightCategory
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.WeatherCondition
import com.defenseunicorns.flyaware.model.WeatherIntensity
import java.time.ZonedDateTime

@Composable
fun AirportsScreen(
    viewModel: AirportsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val metars by viewModel.metars.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val deleteCallback : (String) -> Unit = {viewModel.deleteAirport(it) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
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
                Text ("Airport Metars", modifier.padding(start = 16.dp))
                if (metars.isEmpty()) {
                    Text(
                        text = "No data available",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                LazyColumn(modifier = modifier.padding(16.dp)) {

                    item {
                        metars.forEach { metar ->
                            MetarItem(metar = metar, deleteCallback)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetarItem(metar: Metar, deleteCallback : (String) -> Unit) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Text("Station: ${metar.icaoCode}")
                Text("Flight Category: ${metar.flightCategory}")
                Text("Temperature: ${metar.temperature}°C")
                Text("Wind: ${metar.windSpeed ?: "--"} kt")
                Text("Visibility: ${metar.visibility ?: "--"} mi")
            }
            Column {
                Text("Dewpoint: ${metar.dewpoint}°C")
                Text("Altimeter: ${metar.altimeter ?: "--"} hPa")
                if (metar.cloudLayers.isNotEmpty()) {
                    Text("Cloud Layers:")
                    metar.cloudLayers.forEach { cloudLayer ->
                        Text("- ${cloudLayer.coverage.name} at ${cloudLayer.baseAltitudeFeet} ft")
                    }
                }
            }
        }
        IconButton(
            onClick = {
                deleteCallback.invoke(metar.icaoCode)
            },
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview
@Composable
fun MetrItemPreview() {
    MetarItem(
        Metar(
            icaoCode = "Test",
            rawText = "test text",
            observationTime = ZonedDateTime.now(),
            temperature = 22.0,
            dewpoint = 46.2,
            windDirection = 45,
            windSpeed = 22,
            windGust = 32,
            visibility = 1.5,
            altimeter = 33.3,
            flightCategory = FlightCategory.IFR,
            cloudLayers = listOf(CloudLayer(
                CloudCoverage.VV,
                baseAltitudeFeet = 300,
                cloudType = "tasty"
            )),
            weatherConditions = listOf(WeatherCondition(WeatherIntensity.MODERATE))
        ),
        {}
    )
}
