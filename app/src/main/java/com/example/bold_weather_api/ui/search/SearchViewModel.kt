package com.example.bold_weather_api.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.domain.usecase.GetAllLocationsUseCase
import com.example.bold_weather_api.ui.search.components.LocationRowUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAllLocations: GetAllLocationsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading())
    val uiState: StateFlow<SearchUiState> = _uiState

    init {
        load()
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
        load()
    }

    private fun load() {
        val currentQuery = _uiState.value.query
        _uiState.value = SearchUiState.Loading(query = currentQuery)

        viewModelScope.launch {
            try {
                val locations = getAllLocations()
                _uiState.value = SearchUiState.Success(
                    query = currentQuery,
                    locations = locations.map { it.toRowUi() },
                )
            } catch (t: Throwable) {
                _uiState.value = SearchUiState.Error(
                    query = currentQuery,
                    message = t.message ?: "Unexpected error",
                )
            }
        }
    }

    private fun Location.toRowUi(): LocationRowUi =
        LocationRowUi(
            name = name,
            country = country,
        )
}
