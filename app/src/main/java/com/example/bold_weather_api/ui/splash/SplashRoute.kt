package com.example.bold_weather_api.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    onFinished: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1200)
        onFinished()
    }

    SplashScreen()
}


