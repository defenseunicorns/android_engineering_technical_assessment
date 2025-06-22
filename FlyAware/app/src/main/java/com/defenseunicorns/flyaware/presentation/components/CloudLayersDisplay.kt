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
import com.defenseunicorns.flyaware.model.CloudCoverage
import com.defenseunicorns.flyaware.model.CloudLayer

@Composable
fun CloudLayersDisplay(
    modifier: Modifier = Modifier,
    cloudLayers: List<CloudLayer>,
    title: String = "Clouds"
) {
    Column(modifier = modifier) {
        if (cloudLayers.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cloud),
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
            
            cloudLayers.forEach { cloud ->
                CloudLayerItem(cloud = cloud)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cloud),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$title: No significant clouds",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CloudLayerItem(cloud: CloudLayer) {
    val cloudDescription = when (cloud.coverage) {
        CloudCoverage.SKC -> "SKC (Sky Clear)"
        CloudCoverage.CLR -> "CLR (Clear)"
        CloudCoverage.FEW -> "FEW (Few)"
        CloudCoverage.SCT -> "SCT (Scattered)"
        CloudCoverage.BKN -> "BKN (Broken)"
        CloudCoverage.OVC -> "OVC (Overcast)"
        CloudCoverage.VV -> "VV (Vertical Visibility)"
        CloudCoverage.UNKNOWN -> "Unknown"
        CloudCoverage.CAVOK -> "Clear and visibility okay"
    }
    
    val cloudText = when {
        cloud.coverage == CloudCoverage.SKC || cloud.coverage == CloudCoverage.CLR -> cloudDescription
        cloud.baseAltitudeFeet != null -> "$cloudDescription at ${cloud.baseAltitudeFeet} ft"
        else -> cloudDescription
    }
    
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
            text = cloudText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Show cloud type if available
        cloud.cloudType?.let { type ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "($type)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
} 