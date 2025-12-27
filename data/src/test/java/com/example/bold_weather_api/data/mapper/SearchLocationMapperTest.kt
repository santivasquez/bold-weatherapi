package com.example.bold_weather_api.data.mapper

import com.example.bold_weather_api.data.remote.dto.SearchLocationDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SearchLocationMapperTest {

    @Test
    fun `toDomain returns null when name is null`() {
        val dto = SearchLocationDto(
            id = 1,
            name = null,
            country = "Colombia",
            lat = 1.0,
            lon = 2.0,
        )

        assertNull(dto.toDomain())
    }

    @Test
    fun `toDomain returns null when country is blank`() {
        val dto = SearchLocationDto(
            id = 1,
            name = "Bogota",
            country = "   ",
            lat = 1.0,
            lon = 2.0,
        )

        assertNull(dto.toDomain())
    }

    @Test
    fun `toDomain trims name and country`() {
        val dto = SearchLocationDto(
            id = 10,
            name = "  Medellin  ",
            country = "  Colombia ",
            lat = 6.29,
            lon = -75.54,
        )

        val domain = dto.toDomain()
        assertNotNull(domain)
        assertEquals(10, domain!!.id)
        assertEquals("Medellin", domain.name)
        assertEquals("Colombia", domain.country)
        assertEquals(6.29, domain.lat!!, 0.00001)
        assertEquals(-75.54, domain.lon!!, 0.00001)
    }
}


