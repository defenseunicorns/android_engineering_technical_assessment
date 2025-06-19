package com.defenseunicorns.flyaware.airportdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.defenseunicorns.flyaware.R
import com.defenseunicorns.flyaware.core.model.AirportMetar
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme

@Preview(showBackground = true)
@Composable
fun AirportDetailsScreenPreview() {
    FlyAwareTheme {
        //
    }
}

@Composable
fun AirportDetailsScreen(
    modifier: Modifier = Modifier,
    airportMetar: AirportMetar?
) {
    if (airportMetar == null) return

    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = airportMetar.airport.name)

            airportMetar.metar?.rawText?.let {
                Text(text = it)
            }

            airportMetar.metar?.visibility?.let {
                Text(text = stringResource(R.string.card_visibility, it))
            }

            airportMetar.metar?.temperature?.let {
                Text(text = stringResource(R.string.card_temp, it))
            }

            airportMetar.metar?.dewpoint?.let {
                Text(text = stringResource(R.string.card_dew, it))
            }
        }
    }
}