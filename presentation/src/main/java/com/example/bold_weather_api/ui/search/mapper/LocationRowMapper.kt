package com.example.bold_weather_api.ui.search.mapper

import com.example.bold_weather_api.domain.model.Location
import com.example.bold_weather_api.ui.search.components.LocationRowUi

fun Location.toRowUi(): LocationRowUi =
    LocationRowUi(
        name = name,
        country = country,
        lat = lat,
        lon = lon,
    )


