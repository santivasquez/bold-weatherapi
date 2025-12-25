package com.example.bold_weather_api.ui.search

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchRoute() {
    val viewModel: SearchViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state.value,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::retry,
    )
}
