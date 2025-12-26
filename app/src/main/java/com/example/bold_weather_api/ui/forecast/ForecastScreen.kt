package com.example.bold_weather_api.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bold_weather_api.R
import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.model.ForecastDay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Forecast") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {
            val isWide = maxWidth >= 600.dp

            when (state) {
                ForecastUiState.Loading -> {
                    Text(
                        text = "Loading…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                is ForecastUiState.Error -> {
                    Text(
                        text = "Something went wrong",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(onClick = onRetry) { Text("Retry") }
                }

                is ForecastUiState.Success -> {
                    ForecastContent(
                        forecast = state.forecast,
                        isWide = isWide,
                    )
                }
            }
        }
    }
}

@Composable
private fun ForecastContent(
    forecast: Forecast,
    isWide: Boolean,
) {
    if (!isWide) {
        Column(modifier = Modifier.fillMaxSize()) {
            ForecastHeader(forecast = forecast)
            Spacer(modifier = Modifier.height(10.dp))
            CurrentCard(forecast = forecast)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Next days",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ForecastDaysList(
                days = forecast.days,
                modifier = Modifier.weight(1f),
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {
                ForecastHeader(forecast = forecast)
                Spacer(modifier = Modifier.height(10.dp))
                CurrentCard(forecast = forecast)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Next days",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ForecastDaysList(
                    days = forecast.days,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ForecastHeader(
    forecast: Forecast,
) {
    Text(
        text = "${forecast.locationName}, ${forecast.country}",
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun CurrentCard(
    forecast: Forecast,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Current",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${forecast.currentTempC} °C",
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = forecast.currentCondition.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
            WeatherIcon(url = forecast.currentCondition.iconUrl)
        }
    }
}

@Composable
private fun ForecastDaysList(
    days: List<ForecastDay>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        items(days) { day ->
            ForecastDayRow(day = day)
        }
    }
}

@Composable
private fun ForecastDayRow(
    day: ForecastDay,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = day.date,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Avg: ${day.avgTempC} °C",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = day.condition.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(10.dp))
            WeatherIcon(url = day.condition.iconUrl)
        }
    }
}

@Composable
private fun WeatherIcon(
    url: String?,
    modifier: Modifier = Modifier,
) {
    if (url.isNullOrBlank()) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = modifier.height(44.dp),
            contentScale = ContentScale.Fit,
        )
        return
    }

    AsyncImage(
        model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier.height(44.dp),
        contentScale = ContentScale.Fit,
        placeholder = painterResource(R.drawable.ic_launcher_foreground),
        error = painterResource(R.drawable.ic_launcher_foreground),
    )
}
