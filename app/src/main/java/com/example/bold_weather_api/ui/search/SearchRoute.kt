package com.example.bold_weather_api.ui.search

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchRoute() {
    val viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state.value,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::retry,
    )
}


