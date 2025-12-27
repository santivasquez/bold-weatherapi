package com.example.bold_weather_api.ui.forecast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bold_weather_api.core.error.AppError
import com.example.bold_weather_api.core.error.AppException
import com.example.bold_weather_api.domain.usecase.GetForecastUseCase
import com.example.bold_weather_api.navigation.Routes
import com.example.bold_weather_api.presentation.R
import com.example.bold_weather_api.ui.common.UiText
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
            _uiState.value = ForecastUiState.Error(UiText.StringRes(R.string.forecast_invalid_coordinates))
            return
        }

        _uiState.value = ForecastUiState.Loading
        viewModelScope.launch {
            try {
                val forecast = getForecast(lat = safeLat, lon = safeLon, days = 3)
                _uiState.value = ForecastUiState.Success(forecast)
            } catch (t: Throwable) {
                val appError = (t as? AppException)?.error ?: AppError.Unknown(t.message)
                _uiState.value = ForecastUiState.Error(appError.toUiText())
            }
        }
    }

    private fun AppError.toUiText(): UiText =
        when (this) {
            AppError.Network -> UiText.StringRes(R.string.error_network)
            AppError.Timeout -> UiText.StringRes(R.string.error_timeout)
            is AppError.Http -> when (code) {
                401 -> UiText.StringRes(R.string.error_unauthorized_401)
                403 -> UiText.StringRes(R.string.error_forbidden_403)
                429 -> UiText.StringRes(R.string.error_rate_limit_429)
                else -> UiText.StringRes(R.string.error_http_generic_with_code, args = listOf(code))
            }
            AppError.Serialization -> UiText.StringRes(R.string.error_serialization)
            is AppError.Unknown -> message?.let { UiText.Dynamic(it) } ?: UiText.StringRes(R.string.error_unknown)
        }
}


