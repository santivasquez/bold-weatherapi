package com.example.bold_weather_api.navigation

import java.util.Locale

object Routes {
    const val SPLASH = "splash"
    const val SEARCH = "search"

    const val FORECAST = "forecast/{lat}/{lon}"
    const val ARG_LAT = "lat"
    const val ARG_LON = "lon"

    fun forecast(lat: Double, lon: Double): String {
        // Force dot as decimal separator regardless of device locale.
        val latStr = String.format(Locale.US, "%.4f", lat)
        val lonStr = String.format(Locale.US, "%.4f", lon)
        return "forecast/$latStr/$lonStr"
    }
}


