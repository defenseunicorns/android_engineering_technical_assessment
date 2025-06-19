package com.defenseunicorns.flyaware.airportlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.core.model.Airport
import com.defenseunicorns.flyaware.core.model.AirportMetar
import com.defenseunicorns.flyaware.core.model.FlightCategory
import com.defenseunicorns.flyaware.core.model.Metar
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme
import java.time.ZonedDateTime

@Preview
@Composable
fun AirportCardPreview() {
    FlyAwareTheme {
        AirportCard(
            airportMetar = AirportMetar(
                airport = Airport(
                    icaoCode = "KPDX",
                    name = "Portland International Airport"
                ),
                metar = Metar(
                    icaoCode = "KPDX",
                    rawText = "Hello",
                    observationTime = ZonedDateTime.now(),
                    temperature = 35.0,
                    dewpoint = 1.0,
                    windDirection = null,
                    windSpeed = null,
                    windGust = null,
                    visibility = "10",
                    altimeter = 1.0,
                    flightCategory = FlightCategory.UNKNOWN
                )
            )
        )
    }
}

/**
 * Requested:
 *
 * Latest decoded METAR (including wind, visibility, ceiling, temp)
 * Visual badge or color to indicate current flight rules (VFR/MVFR/IFR/LIFR)
 */
@Composable
fun AirportCard(
    modifier: Modifier = Modifier,
    airportMetar: AirportMetar,
    onClick: (String) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        onClick = { onClick(airportMetar.airport.icaoCode) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                Text(text = airportMetar.airport.icaoCode)
                Spacer(Modifier.size(16.dp))
                Text(text = airportMetar.airport.name)
            }

            airportMetar.metar?.visibility?.let {
                Text(text = stringResource(R.string.card_visibility, it))
            }

            airportMetar.metar?.temperature?.let {
                Text(text = stringResource(R.string.card_temp, it))
            }
        }
    }
}