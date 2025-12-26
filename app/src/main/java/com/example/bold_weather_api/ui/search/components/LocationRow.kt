package com.example.bold_weather_api.ui.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bold_weather_api.ui.theme.BoldweatherapiTheme

@Immutable
data class LocationRowUi(
    val name: String,
    val country: String,
    val lat: Double?,
    val lon: Double?,
)

@Composable
fun LocationRow(
    row: LocationRowUi,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .alpha(if (enabled) 1f else 0.55f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = row.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = row.country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 120)
@Composable
private fun LocationRowPreview() {
    BoldweatherapiTheme {
        LocationRow(
            row = LocationRowUi(name = "Bogotá", country = "Colombia", lat = 4.7110, lon = -74.0721),
            onClick = {},
            enabled = true,
            modifier = Modifier.padding(16.dp),
        )
    }
}
