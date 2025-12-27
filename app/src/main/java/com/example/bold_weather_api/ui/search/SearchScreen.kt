package com.example.bold_weather_api.ui.search

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bold_weather_api.R
import com.example.bold_weather_api.ui.common.InfoDialog
import com.example.bold_weather_api.ui.common.LoadingOverlay
import com.example.bold_weather_api.ui.common.UiText
import com.example.bold_weather_api.ui.search.components.LocationRow
import com.example.bold_weather_api.ui.theme.BoldweatherapiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onRetry: () -> Unit,
    onLocationSelected: (com.example.bold_weather_api.ui.search.components.LocationRowUi) -> Unit,
    onUseMyLocation: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showApiErrorDialog = remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        showApiErrorDialog.value = state is SearchUiState.Error
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.search_title)) },
            )
        },
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxSize(),
        ) {
            val isWide = maxWidth >= 600.dp

            Box(modifier = Modifier.fillMaxSize()) {
                if (!isWide) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        SearchHeader(
                            query = state.query,
                            onQueryChanged = onQueryChanged,
                            onUseMyLocation = onUseMyLocation,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SearchBody(
                            state = state,
                            onLocationSelected = onLocationSelected,
                        )
                    }
                } else {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                        ) {
                            SearchHeader(
                                query = state.query,
                                onQueryChanged = onQueryChanged,
                                onUseMyLocation = onUseMyLocation,
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                        ) {
                            SearchBody(
                                state = state,
                                onLocationSelected = onLocationSelected,
                            )
                        }
                    }
                }

                if (state is SearchUiState.Loading) {
                    LoadingOverlay()
                }
            }
        }
    }

    // API errors (search)
    val apiErrorMessage = (state as? SearchUiState.Error)?.message
    if (apiErrorMessage != null && showApiErrorDialog.value) {
        InfoDialog(
            title = UiText.StringRes(R.string.common_something_went_wrong),
            message = apiErrorMessage,
            onDismiss = { showApiErrorDialog.value = false },
            onRetry = {
                showApiErrorDialog.value = false
                onRetry()
            }
        )
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onUseMyLocation: () -> Unit,
) {
    Text(
        text = stringResource(R.string.search_find_location),
        style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        singleLine = true,
    )
    Spacer(modifier = Modifier.height(10.dp))
    Button(
        onClick = onUseMyLocation,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(stringResource(R.string.search_use_my_location))
    }
}

@Composable
private fun SearchBody(
    state: SearchUiState,
    onLocationSelected: (com.example.bold_weather_api.ui.search.components.LocationRowUi) -> Unit,
) {
    when (state) {
        is SearchUiState.Loading -> {
            // Keep content visible; loading overlay is handled by parent.
        }

        is SearchUiState.Error -> {
            // Dialog is handled by parent.
        }

        is SearchUiState.Success -> {
            Text(
                text = stringResource(R.string.search_results),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(state.locations) { row ->
                    LocationRow(
                        row = row,
                        enabled = row.lat != null && row.lon != null,
                        onClick = { onLocationSelected(row) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SearchScreenPreview() {
    BoldweatherapiTheme {
        SearchScreen(
            state = SearchUiState.Success(
                query = "",
                locations = emptyList(),
            ),
            onQueryChanged = {},
            onRetry = {},
            onLocationSelected = {},
            onUseMyLocation = {},
        )
    }
}
