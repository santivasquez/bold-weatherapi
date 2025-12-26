package com.example.bold_weather_api.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bold_weather_api.core.error.AppError
import com.example.bold_weather_api.core.error.AppException
import com.example.bold_weather_api.domain.usecase.SearchLocationsUseCase
import com.example.bold_weather_api.ui.search.mapper.toRowUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.FlowPreview
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
                _uiState.value = SearchUiState.Error(query = query, message = message)
            }
        }
    }
}
