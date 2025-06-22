package com.defenseunicorns.flyaware.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.model.WeatherCondition
import com.defenseunicorns.flyaware.model.WeatherIntensity

@Composable
fun WeatherConditionsDisplay(
    modifier: Modifier = Modifier,
    weatherConditions: List<WeatherCondition>,
    title: String = "Weather"
    ) {
    Column(modifier = modifier) {
        if (weatherConditions.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear_day),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            weatherConditions.forEach { condition ->
                WeatherConditionItem(condition = condition)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_clear_day),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$title: No significant weather",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun WeatherConditionItem(condition: WeatherCondition) {
    val intensityText = when (condition.intensity) {
        WeatherIntensity.LIGHT -> "Light"
        WeatherIntensity.MODERATE -> "Moderate"
        WeatherIntensity.HEAVY -> "Heavy"
        WeatherIntensity.VICINITY -> "Vicinity"
        WeatherIntensity.UNKNOWN -> ""
        null -> ""
    }

    val descriptorText = condition.descriptor ?: ""
    val phenomenaText = condition.phenomena.joinToString(" ")

    val fullDescription = listOfNotNull(intensityText, descriptorText, phenomenaText)
        .joinToString(" ")
        .trim()

    if (fullDescription.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "•",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = fullDescription,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 