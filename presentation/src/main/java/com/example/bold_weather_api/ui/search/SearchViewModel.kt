package com.example.bold_weather_api.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bold_weather_api.core.error.AppError
import com.example.bold_weather_api.core.error.AppException
import com.example.bold_weather_api.domain.usecase.SearchLocationsUseCase
import com.example.bold_weather_api.presentation.R
import com.example.bold_weather_api.ui.common.UiText
import com.example.bold_weather_api.ui.search.mapper.toRowUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchLocations: SearchLocationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(
        SearchUiState.Success(query = "", locations = emptyList())
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        observeQuery()
    }

    fun onQueryChanged(value: String) {
        _uiState.update { current ->
            when (current) {
                is SearchUiState.Loading -> current.copy(query = value)
                is SearchUiState.Success -> current.copy(query = value)
                is SearchUiState.Error -> current.copy(query = value)
            }
        }
    }

    fun retry() {
        val query = _uiState.value.query.trim()
        if (query.isNotBlank()) performSearch(query)
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            uiState
                .map { it.query.trim() }
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = SearchUiState.Success(query = "", locations = emptyList())
                    } else {
                        performSearch(query)
                    }
                }
        }
    }

    private fun performSearch(query: String) {
        _uiState.value = SearchUiState.Loading(query = query)

        viewModelScope.launch {
            try {
                val locations = searchLocations(query)
                _uiState.value = SearchUiState.Success(
                    query = query,
                    locations = locations.map { it.toRowUi() },
                )
            } catch (throwable: Throwable) {
                val appError = (throwable as? AppException)?.error ?: AppError.Unknown(throwable.message)
                _uiState.value = SearchUiState.Error(query = query, message = appError.toUiText())
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


