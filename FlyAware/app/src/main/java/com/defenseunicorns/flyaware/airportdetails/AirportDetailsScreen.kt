package com.defenseunicorns.flyaware.airportdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    airportId: String
) {
    Text(
        text = "AirportDetails",
        modifier = modifier
            .fillMaxSize()
    )
}