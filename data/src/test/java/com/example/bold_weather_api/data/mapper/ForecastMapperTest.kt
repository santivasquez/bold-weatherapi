package com.example.bold_weather_api.data.mapper

import com.example.bold_weather_api.data.remote.dto.ConditionDto
import com.example.bold_weather_api.data.remote.dto.CurrentDto
import com.example.bold_weather_api.data.remote.dto.DayDto
import com.example.bold_weather_api.data.remote.dto.ForecastDayDto
import com.example.bold_weather_api.data.remote.dto.ForecastDto
import com.example.bold_weather_api.data.remote.dto.ForecastLocationDto
import com.example.bold_weather_api.data.remote.dto.ForecastResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ForecastMapperTest {

    @Test
    fun `toDomain returns null when location is missing`() {
        val dto = ForecastResponseDto(
            location = null,
            current = CurrentDto(tempC = 24.0, condition = ConditionDto(text = "Sunny", icon = "//cdn/x.png")),
            forecast = null,
        )

        assertNull(dto.toDomain())
    }

    @Test
    fun `toDomain maps current and forecast days and expands icon url`() {
        val dto = ForecastResponseDto(
            location = ForecastLocationDto(
                name = "Medellin",
                country = "Colombia",
                lat = 6.29,
                lon = -75.54,
            ),
            current = CurrentDto(
                tempC = 25.5,
                condition = ConditionDto(text = "Partly cloudy", icon = "//cdn.weatherapi.com/icon.png"),
            ),
            forecast = ForecastDto(
                forecastDays = listOf(
                    ForecastDayDto(
                        date = "2025-12-26",
                        day = DayDto(
                            avgTempC = 22.0,
                            condition = ConditionDto(text = "Sunny", icon = "//cdn.weatherapi.com/day1.png"),
                        ),
                    ),
                    ForecastDayDto(
                        date = "2025-12-27",
                        day = DayDto(
                            avgTempC = 21.0,
                            condition = ConditionDto(text = "Rain", icon = "https://cdn.weatherapi.com/day2.png"),
                        ),
                    ),
                )
            )
        )

        val domain = dto.toDomain()
        assertNotNull(domain)

        val forecast = domain!!
        assertEquals("Medellin", forecast.locationName)
        assertEquals("Colombia", forecast.country)
        assertEquals(25.5, forecast.currentTempC, 0.00001)
        assertEquals("Partly cloudy", forecast.currentCondition.text)
        assertEquals("https://cdn.weatherapi.com/icon.png", forecast.currentCondition.iconUrl)

        assertEquals(2, forecast.days.size)
        assertEquals("2025-12-26", forecast.days[0].date)
        assertEquals(22.0, forecast.days[0].avgTempC, 0.00001)
        assertEquals("Sunny", forecast.days[0].condition.text)
        assertEquals("https://cdn.weatherapi.com/day1.png", forecast.days[0].condition.iconUrl)

        assertEquals("2025-12-27", forecast.days[1].date)
        assertEquals("Rain", forecast.days[1].condition.text)
        assertEquals("https://cdn.weatherapi.com/day2.png", forecast.days[1].condition.iconUrl)
    }
}


