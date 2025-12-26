package com.example.bold_weather_api.domain.model

data class Forecast(
    val locationName: String,
    val country: String,
    val currentTempC: Double,
    val currentCondition: Condition,
    val days: List<ForecastDay>,
)
