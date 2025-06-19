package com.defenseunicorns.flyaware.airportlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.defenseunicorns.flyaware.R

@Composable
fun ContentEmpty(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.airports_empty),
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center)
    )
}