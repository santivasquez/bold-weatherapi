package com.example.bold_weather_api.domain.model

data class Location(
    val id: Int? = null,
    val name: String,
    val country: String,
    val lat: Double? = null,
    val lon: Double? = null,
)
