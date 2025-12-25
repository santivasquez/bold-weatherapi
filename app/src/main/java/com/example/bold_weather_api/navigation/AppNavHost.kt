package com.example.bold_weather_api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bold_weather_api.ui.search.SearchRoute
import com.example.bold_weather_api.ui.splash.SplashRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash,
        modifier = modifier,
    ) {
        composable(Routes.Splash) {
            SplashRoute(
                onFinished = {
                    navController.navigate(Routes.Search) {
                        popUpTo(Routes.Splash) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.Search) {
            SearchRoute()
        }
    }
}


