package com.example.bold_weather_api.domain.model

data class ForecastDay(
    val date: String,
    val avgTempC: Double,
    val condition: Condition,
)
