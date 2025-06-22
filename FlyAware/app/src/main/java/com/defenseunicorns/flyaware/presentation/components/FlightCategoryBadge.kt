package com.defenseunicorns.flyaware.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.defenseunicorns.flyaware.model.FlightCategory
import com.defenseunicorns.flyaware.ui.theme.FlightCategoryColors

@Composable
fun FlightCategoryBadge(
    flightCategory: FlightCategory,
    modifier: Modifier = Modifier
) {
    val backgroundColor = FlightCategoryColors.getBackgroundColor(flightCategory)
    val textColor = FlightCategoryColors.getColor(flightCategory)
    val displayText = FlightCategoryColors.getDisplayName(flightCategory)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )
    }
} 