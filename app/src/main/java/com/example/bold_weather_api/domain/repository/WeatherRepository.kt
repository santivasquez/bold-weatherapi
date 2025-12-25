package com.example.bold_weather_api.domain.repository

import com.example.bold_weather_api.domain.model.Location

interface WeatherRepository {
    suspend fun getAllLocations(): List<Location>
}


