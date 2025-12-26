package com.example.bold_weather_api.ui.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bold_weather_api.core.error.AppError
import com.example.bold_weather_api.core.error.AppException
import com.example.bold_weather_api.domain.usecase.GetForecastUseCase
import com.example.bold_weather_api.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecast: GetForecastUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState: StateFlow<ForecastUiState> = _uiState

    private val lat: Double?
        get() = savedStateHandle.get<Float>(Routes.ARG_LAT)?.toDouble()

    private val lon: Double?
        get() = savedStateHandle.get<Float>(Routes.ARG_LON)?.toDouble()

    init {
        load()
    }

    fun retry() {
        load()
    }

    private fun load() {
        val safeLat = lat
        val safeLon = lon
        if (safeLat == null || safeLon == null) {
            _uiState.value = ForecastUiState.Error("Invalid location coordinates.")
            return
        }

        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            try {
                val forecast = getForecast(lat = safeLat, lon = safeLon, days = 3)
                _uiState.value = ForecastUiState.Success(forecast)
            } catch (t: Throwable) {
                val appError = (t as? AppException)?.error ?: AppError.Unknown(t.message)
                val message = when (appError) {
                    AppError.Network -> "Network error. Check your connection."
                    AppError.Timeout -> "Request timed out. Please try again."
                    is AppError.Http -> when (appError.code) {
                        401 -> "Unauthorized (401). Check WEATHER_API_KEY in local.properties."
                        403 -> "Forbidden (403). Check if the API key has permissions."
                        429 -> "Rate limit (429). Please wait and try again."
                        else -> "Request failed (${appError.code}). Please try again."
                    }
                    AppError.Serialization -> "Unexpected response format."
                    is AppError.Unknown -> appError.message ?: "Unexpected error"
                }
                _uiState.value = ForecastUiState.Error(message)
            }
        }
    }
}
