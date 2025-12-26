package com.example.bold_weather_api.ui.search

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bold_weather_api.ui.search.components.LocationRowUi
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SearchRoute(
    onLocationSelected: (LocationRowUi) -> Unit,
    onNavigateToForecast: (lat: Double, lon: Double) -> Unit,
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationError = remember { mutableStateOf<String?>(null) }

    fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    suspend fun fetchAndNavigateToCurrentLocation() {
        try {
            val client = LocationServices.getFusedLocationProviderClient(context)
            val location = client.lastLocation.await()
            if (location == null) {
                locationError.value = "Couldn't get current location. Try again."
                return
            }
            locationError.value = null
            onNavigateToForecast(location.latitude, location.longitude)
        } catch (t: Throwable) {
            locationError.value = t.message ?: "Couldn't get current location."
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true || result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            scope.launch { fetchAndNavigateToCurrentLocation() }
        } else {
            locationError.value = "Location permission denied."
        }
    }

    SearchScreen(
        state = state.value,
        onQueryChanged = viewModel::onQueryChanged,
        onRetry = viewModel::retry,
        onLocationSelected = onLocationSelected,
        onUseMyLocation = {
            locationError.value = null
            if (hasLocationPermission()) {
                scope.launch { fetchAndNavigateToCurrentLocation() }
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    )
                )
            }
        },
        locationErrorMessage = locationError.value,
    )
}
