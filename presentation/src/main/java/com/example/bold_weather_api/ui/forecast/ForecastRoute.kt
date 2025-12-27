package com.example.bold_weather_api.ui.forecast

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ForecastRoute(
    onBack: () -> Unit,
) {
    val viewModel: ForecastViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    ForecastScreen(
        state = state.value,
        onBack = onBack,
        onRetry = viewModel::retry,
    )
}


