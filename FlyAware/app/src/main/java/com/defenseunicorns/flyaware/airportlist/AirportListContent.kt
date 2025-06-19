package com.defenseunicorns.flyaware.airportlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.defenseunicorns.flyaware.core.model.AirportMetar

@Composable
fun AirportListContent(
    modifier: Modifier = Modifier,
    content: List<AirportMetar>,
    onClick: (String) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(content) {
            AirportCard(
                airportMetar = it,
                onClick = onClick
            )
        }
    }
}