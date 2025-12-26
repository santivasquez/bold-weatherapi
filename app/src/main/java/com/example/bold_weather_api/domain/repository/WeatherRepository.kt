package com.example.bold_weather_api.domain.repository

import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.model.Location

interface WeatherRepository {
    suspend fun searchLocations(query: String): List<Location>

    suspend fun getForecast(
        lat: Double,
        lon: Double,
        days: Int,
    ): Forecast
}
