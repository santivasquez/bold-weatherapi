package com.example.bold_weather_api.domain.usecase

import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.domain.repository.WeatherRepository
import javax.inject.Inject

class SearchLocationsUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(query: String): List<Location> =
        weatherRepository.searchLocations(query)
}


