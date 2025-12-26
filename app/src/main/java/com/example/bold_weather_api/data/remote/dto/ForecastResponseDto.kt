package com.example.bold_weather_api.data.remote.dto

import com.squareup.moshi.Json

data class ForecastResponseDto(
    @Json(name = "location") val location: ForecastLocationDto?,
    @Json(name = "current") val current: CurrentDto?,
    @Json(name = "forecast") val forecast: ForecastDto?,
)

data class ForecastLocationDto(
    @Json(name = "name") val name: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lon") val lon: Double?,
)

data class CurrentDto(
    @Json(name = "temp_c") val tempC: Double?,
    @Json(name = "condition") val condition: ConditionDto?,
)

data class ForecastDto(
    @Json(name = "forecastday") val forecastDays: List<ForecastDayDto>?,
)

data class ForecastDayDto(
    @Json(name = "date") val date: String?,
    @Json(name = "day") val day: DayDto?,
)

data class DayDto(
    @Json(name = "avgtemp_c") val avgTempC: Double?,
    @Json(name = "condition") val condition: ConditionDto?,
)

data class ConditionDto(
    @Json(name = "text") val text: String?,
    @Json(name = "icon") val icon: String?,
)
