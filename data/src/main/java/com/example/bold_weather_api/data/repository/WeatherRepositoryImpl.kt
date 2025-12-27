package com.example.bold_weather_api.data.repository

import com.example.bold_weather_api.core.error.safeApiCall
import com.example.bold_weather_api.data.mapper.toDomain
import com.example.bold_weather_api.data.mapper.toDomain as toForecastDomain
import com.example.bold_weather_api.data.remote.WeatherApiService
import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApiService,
) : WeatherRepository {

    override suspend fun searchLocations(query: String): List<Location> =
        safeApiCall {
            api.searchLocations(query).mapNotNull { it.toDomain() }
        }

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        days: Int,
    ): Forecast =
        safeApiCall {
            val q = "$lat,$lon"
            val dto = api.getForecast(query = q, days = days)
            dto.toForecastDomain()
                ?: error("Invalid forecast response")
        }
}


