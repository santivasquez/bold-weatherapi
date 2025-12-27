package com.example.bold_weather_api.data.mapper

import com.example.bold_weather_api.data.remote.dto.ForecastResponseDto
import com.example.bold_weather_api.domain.model.Condition
import com.example.bold_weather_api.domain.model.Forecast
import com.example.bold_weather_api.domain.model.ForecastDay

fun ForecastResponseDto.toDomain(): Forecast? {
    val locationName = location?.name?.trim().orEmpty()
    val country = location?.country?.trim().orEmpty()
    val currentTempC = current?.tempC
    val currentConditionText = current?.condition?.text?.trim().orEmpty()
    val currentIcon = current?.condition?.icon?.toFullIconUrl()

    val days = forecast?.forecastDays.orEmpty().mapNotNull { dto ->
        val date = dto.date?.trim().orEmpty()
        val avgTemp = dto.day?.avgTempC
        val text = dto.day?.condition?.text?.trim().orEmpty()
        val icon = dto.day?.condition?.icon?.toFullIconUrl()
        if (date.isBlank() || avgTemp == null || text.isBlank()) return@mapNotNull null
        ForecastDay(
            date = date,
            avgTempC = avgTemp,
            condition = Condition(text = text, iconUrl = icon),
        )
    }

    if (locationName.isBlank() || country.isBlank()) return null
    if (currentTempC == null || currentConditionText.isBlank()) return null

    return Forecast(
        locationName = locationName,
        country = country,
        currentTempC = currentTempC,
        currentCondition = Condition(text = currentConditionText, iconUrl = currentIcon),
        days = days,
    )
}

private fun String?.toFullIconUrl(): String? {
    val raw = this?.trim().orEmpty()
    if (raw.isBlank()) return null
    return if (raw.startsWith("//")) "https:$raw" else raw
}


