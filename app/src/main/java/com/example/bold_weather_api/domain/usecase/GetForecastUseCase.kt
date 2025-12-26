package com.example.bold_weather_api.domain.usecase

import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        days: Int = 3,
    ): Forecast = weatherRepository.getForecast(lat = lat, lon = lon, days = days)
}
