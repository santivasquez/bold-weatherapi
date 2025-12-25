package com.example.bold_weather_api.ui.search

import com.example.bold_weather_api.ui.search.components.LocationRowUi

sealed interface SearchUiState {
    val query: String

    data class Loading(
        override val query: String = "",
    ) : SearchUiState

    data class Success(
        override val query: String = "",
        val locations: List<LocationRowUi> = emptyList(),
    ) : SearchUiState

    data class Error(
        override val query: String = "",
        val message: String,
    ) : SearchUiState
}
