package com.example.bold_weather_api.ui.search.mapper

import com.example.bold_weather_api.domain.model.Location
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationRowMapperTest {
    @Test
    fun `toRowUi passes through lat lon`() {
        val domain = Location(
            id = 1,
            name = "Bogota",
            country = "Colombia",
            lat = 4.7110,
            lon = -74.0721,
        )

        val ui = domain.toRowUi()
        assertEquals("Bogota", ui.name)
        assertEquals("Colombia", ui.country)
        assertEquals(4.7110, ui.lat!!, 0.00001)
        assertEquals(-74.0721, ui.lon!!, 0.00001)
    }
}
