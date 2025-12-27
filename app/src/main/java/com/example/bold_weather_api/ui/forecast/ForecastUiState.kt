package com.example.bold_weather_api.ui.forecast

import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.ui.common.UiText

sealed interface ForecastUiState {
    data object Loading : ForecastUiState
    data class Success(val forecast: Forecast) : ForecastUiState
    data class Error(val message: UiText) : ForecastUiState
}
