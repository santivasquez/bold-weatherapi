package com.example.bold_weather_api.data.mapper

import com.example.bold_weather_api.data.remote.dto.SearchLocationDto
import com.example.bold_weather_api.domain.model.Location

fun SearchLocationDto.toDomain(): Location? {
    val safeName = name?.trim().orEmpty()
    val safeCountry = country?.trim().orEmpty()

    if (safeName.isBlank() || safeCountry.isBlank()) return null

    return Location(
        id = id,
        name = safeName,
        country = safeCountry,
        lat = lat,
        lon = lon,
    )
}


