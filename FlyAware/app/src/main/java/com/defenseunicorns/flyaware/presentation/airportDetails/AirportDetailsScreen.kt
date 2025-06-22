package com.defenseunicorns.flyaware.presentation.airportDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.model.ForecastPeriod
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.Taf
import com.defenseunicorns.flyaware.presentation.components.AirportInfoCard
import com.defenseunicorns.flyaware.presentation.components.CloudLayersDisplay
import com.defenseunicorns.flyaware.presentation.components.FlightCategoryBadge
import com.defenseunicorns.flyaware.presentation.components.WeatherConditionsDisplay
import com.defenseunicorns.flyaware.presentation.components.WeatherDetailsGrid
import com.defenseunicorns.flyaware.presentation.utils.formatWind
import java.time.format.DateTimeFormatter

@Composable
fun AirportDetailsScreen(
    modifier: Modifier = Modifier,
    icaoId: String,
    viewModel: AirportDetailsViewModel = hiltViewModel()
) {
    val metar by viewModel.metar.collectAsState()
    val taf by viewModel.taf.collectAsState()
    val airport by viewModel.airport.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val tafError by viewModel.tafError.collectAsState()

    LaunchedEffect(icaoId) {
        viewModel.loadAirportData(icaoId)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading airport data...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            error != null && metar == null -> {
                // Only show error if we have no METAR data
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_error),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error loading data",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        AirportInfoCard(airport = airport, metar = metar)
                    }
                    item {
                        MetarSection(metar = metar)
                    }
                    item {
                        TafSection(taf = taf, tafError = tafError)
                    }
                }
            }
        }
    }
}

@Composable
private fun MetarSection(metar: Metar?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current Conditions (METAR)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                metar?.let { FlightCategoryBadge(flightCategory = it.flightCategory) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            metar?.let { m ->
                // Raw METAR text
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = m.rawText,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed weather information
                WeatherDetailsGrid(metar = m, showClowdLayers = true, showWeatherConditions = true)
            } ?: run {
                Text(
                    text = "No METAR data available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TafSection(taf: Taf?, tafError: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Forecast (TAF)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                tafError != null -> {
                    // Show TAF error
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "TAF Data Unavailable",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tafError,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                taf != null -> {
                    // Raw TAF text
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = taf.rawText,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // TAF validity period
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Valid From",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = taf.validFromTime.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Column {
                            Text(
                                text = "Valid Until",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = taf.validUntilTime.format(DateTimeFormatter.ofPattern("MMM dd HH:mm")),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Forecast periods (if available)
                    if (taf.forecastPeriods.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Forecast Periods",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        taf.forecastPeriods.take(6).forEach { period ->
                            ForecastPeriodCard(period = period)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                else -> {
                    Text(
                        text = "No TAF forecast available for this airport",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "TAF forecasts are not available for all airports or may be temporarily unavailable",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastPeriodCard(period: ForecastPeriod) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${period.fromTime.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${
                        period.untilTime.format(
                            DateTimeFormatter.ofPattern("HH:mm")
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                FlightCategoryBadge(flightCategory = period.flightCategory)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Wind information
            if (period.windDirection != null || period.windSpeed != null) {
                Text(
                    text = "Wind: ${
                        formatWind(
                            period.windDirection,
                            period.windSpeed,
                            period.windGust
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Visibility information
            if (period.visibility != null) {
                Text(
                    text = "Visibility: ${period.visibility} mi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Cloud layers information
            if (period.cloudLayers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                CloudLayersDisplay(
                    cloudLayers = period.cloudLayers,
                    title = "Clouds"
                )
            } else {
                // Show "No significant clouds" if no cloud layers are reported
                Spacer(modifier = Modifier.height(4.dp))
                CloudLayersDisplay(
                    cloudLayers = emptyList(),
                    title = "Clouds"
                )
            }

            // Weather conditions information
            if (period.weatherConditions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                WeatherConditionsDisplay(
                    weatherConditions = period.weatherConditions,
                    title = "Weather Conditions"
                )
            }

            // Change indicator
            if (period.changeIndicator != com.defenseunicorns.flyaware.model.ChangeIndicator.NONE) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Change: ${period.changeIndicator.name}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

