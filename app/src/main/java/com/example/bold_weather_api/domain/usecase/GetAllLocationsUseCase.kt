package com.example.bold_weather_api.domain.usecase

import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.domain.repository.WeatherRepository

class GetAllLocationsUseCase(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(): List<Location> = weatherRepository.getAllLocations()
}


