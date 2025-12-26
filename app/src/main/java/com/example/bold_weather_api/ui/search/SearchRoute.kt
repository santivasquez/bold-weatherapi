package com.example.bold_weather_api.ui.search

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bold_weather_api.ui.search.components.LocationRowUi

@Composable
fun SearchRoute(
    onLocationSelected: (LocationRowUi) -> Unit,
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        state = state.value,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::retry,
        onLocationSelected = onLocationSelected,
    )
}
