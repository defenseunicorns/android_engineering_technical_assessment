package com.defenseunicorns.flyaware.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.presentation.utils.formatWind

@Composable
fun WeatherDetailsGrid(
    metar: Metar,
    showClowdLayers: Boolean,
    showWeatherConditions: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeatherDetailItem(
                painter = painterResource(R.drawable.ic_thermometer),
                label = "Temperature",
                value = "${metar.temperature?.toInt() ?: "--"}°C",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                painter = painterResource(R.drawable.ic_dew_point),
                label = "Dewpoint",
                value = "${metar.dewpoint?.toInt() ?: "--"}°C",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                painter = painterResource(R.drawable.ic_wind_power),
                label = "Wind",
                value = formatWind(metar.windDirection, metar.windSpeed, metar.windGust),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WeatherDetailItem(
                painter = painterResource(R.drawable.ic_altimeter),
                label = "Altimeter",
                value = "${metar.altimeter ?: "--"} hPa",
                modifier = Modifier.weight(1f)
            )
            WeatherDetailItem(
                painter = painterResource(R.drawable.ic_visibility),
                label = "Visibility",
                value = "${metar.visibility ?: "--"} mi",
                modifier = Modifier.weight(1f)
            )
            if (metar.cloudLayers.isNotEmpty()) {
                WeatherDetailItem(
                    painter = painterResource(R.drawable.ic_cloud),
                    label = "Ceiling",
                    value = "${metar.cloudLayers.first().baseAltitudeFeet ?: "--"} ft",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Weather conditions display
        if (showWeatherConditions && metar.weatherConditions.isNotEmpty()) {
            WeatherConditionsDisplay(
                weatherConditions = metar.weatherConditions,
                title = "Weather Conditions"
            )
        }

        // Cloud layers using the new component
        if (showClowdLayers) {
            CloudLayersDisplay(
                cloudLayers = metar.cloudLayers,
                title = "Cloud Layers"
            )
        }
    }
}

@Composable
private fun WeatherDetailItem(
    painter: Painter,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}