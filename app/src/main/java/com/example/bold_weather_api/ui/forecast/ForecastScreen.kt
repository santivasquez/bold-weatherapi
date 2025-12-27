package com.example.bold_weather_api.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bold_weather_api.R
import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.model.ForecastDay
import com.example.bold_weather_api.ui.common.asString
import com.example.bold_weather_api.ui.common.InfoDialog
import com.example.bold_weather_api.ui.common.LoadingOverlay
import com.example.bold_weather_api.ui.common.UiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    state: ForecastUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showErrorDialog = remember { mutableStateOf(false) }
    LaunchedEffect(state) {
        showErrorDialog.value = state is ForecastUiState.Error
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.forecast_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.forecast_back_cd),
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

            Box(modifier = Modifier.fillMaxSize()) {
                if (state is ForecastUiState.Success) {
                    ForecastContent(
                        forecast = state.forecast,
                        isWide = isWide,
                    )
                }
                if (state is ForecastUiState.Error) {
                    // Fallback UI so the screen is never "blank" after dismissing the dialog.
                    TextButton(
                        onClick = onRetry,
                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center),
                    ) {
                        Text(stringResource(R.string.common_retry))
                    }
                }
                if (state is ForecastUiState.Loading) {
                    LoadingOverlay()
                }
            }
        }
    }

    val errorMessage = (state as? ForecastUiState.Error)?.message
    if (errorMessage != null && showErrorDialog.value) {
        InfoDialog(
            title = UiText.StringRes(R.string.common_something_went_wrong),
            message = errorMessage,
            onDismiss = { showErrorDialog.value = false },
            onRetry = {
                showErrorDialog.value = false
                onRetry()
            },
        )
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
                text = stringResource(R.string.forecast_next_days_label),
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
                    text = stringResource(R.string.forecast_next_days_label),
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
        text = stringResource(
            R.string.forecast_location_header_format,
            forecast.locationName,
            forecast.country,
        ),
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
                text = stringResource(R.string.forecast_current_label),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.forecast_temp_c_format, forecast.currentTempC),
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
                text = stringResource(R.string.forecast_avg_temp_c_format, day.avgTempC),
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
