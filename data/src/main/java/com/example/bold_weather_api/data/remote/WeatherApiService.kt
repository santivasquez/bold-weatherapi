package com.example.bold_weather_api.data.remote

import com.example.bold_weather_api.data.remote.dto.ForecastResponseDto
import com.example.bold_weather_api.data.remote.dto.SearchLocationDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/search.json")
    suspend fun searchLocations(
        @Query("q") query: String,
    ): List<SearchLocationDto>

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("q") query: String,
        @Query("days") days: Int,
    ): ForecastResponseDto
}


