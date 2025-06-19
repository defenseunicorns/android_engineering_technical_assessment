package com.defenseunicorns.flyaware.presentation.airportDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AirportDetailsScreen(modifier: Modifier, icaoId: String) {

    Column(modifier.fillMaxSize()) {
        Text("details screen for $icaoId")
    }
}