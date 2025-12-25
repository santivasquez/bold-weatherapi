package com.example.bold_weather_api.data.repository

import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.domain.repository.WeatherRepository
import kotlinx.coroutines.delay

class MockWeatherRepository : WeatherRepository {
    override suspend fun getAllLocations(): List<Location> {
        delay(1500)
        return listOf(
            Location(id = 1, name = "Medellín", country = "Colombia", lat = 6.2442, lon = -75.5812),
            Location(id = 2, name = "Bogotá", country = "Colombia", lat = 4.7110, lon = -74.0721),
            Location(id = 3, name = "Paris", country = "France", lat = 48.8566, lon = 2.3522),
            Location(id = 4, name = "Madrid", country = "Spain", lat = 40.4168, lon = -3.7038),
        )
    }
}
