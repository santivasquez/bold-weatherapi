package com.example.bold_weather_api.data.remote.dto

import com.squareup.moshi.Json

data class SearchLocationDto(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "lat") val lat: Double?,
    @Json(name = "lon") val lon: Double?,
)


