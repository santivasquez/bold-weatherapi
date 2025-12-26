package com.example.bold_weather_api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bold_weather_api.ui.forecast.ForecastRoute
import com.example.bold_weather_api.ui.search.SearchRoute
import com.example.bold_weather_api.ui.splash.SplashRoute

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier,
    ) {
        composable(Routes.SPLASH) {
            SplashRoute(
                onFinished = {
                    navController.navigate(Routes.SEARCH) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.SEARCH) {
            SearchRoute(
                onLocationSelected = { row ->
                    val lat = row.lat
                    val lon = row.lon
                    if (lat != null && lon != null) {
                        navController.navigate(Routes.forecast(lat, lon))
                    }
                }
            )
        }
        composable(
            route = Routes.FORECAST,
            arguments = listOf(
                navArgument(Routes.ARG_LAT) { type = NavType.StringType },
                navArgument(Routes.ARG_LON) { type = NavType.StringType },
            ),
        ) {
            ForecastRoute(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
