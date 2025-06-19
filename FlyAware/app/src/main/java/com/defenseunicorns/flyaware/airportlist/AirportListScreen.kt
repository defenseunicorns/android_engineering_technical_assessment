package com.defenseunicorns.flyaware.airportlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.defenseunicorns.flyaware.ui.theme.FlyAwareTheme


@Preview(showBackground = true)
@Composable
fun AirportListScreenPreview() {
    FlyAwareTheme {
        //
    }
}

@Composable
fun AirportListScreen(
    modifier: Modifier = Modifier,
    onAirportSelected: (String) -> Unit
) {
    Text(
        text = "AirportList",
        modifier = modifier
            .fillMaxSize()
    )
}
