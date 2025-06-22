package com.defenseunicorns.flyaware

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.MetarCloudDto
import org.junit.Test
import org.junit.Assert.*

class MetarLatLonTest {

    @Test
    fun `test latitude and longitude parsing from METAR`() {
        // Test case based on the sample METAR data provided
        val metarDto = MetarDto(
            icaoId = "KMCI",
            rawText = "KMCI 212353Z 20018G33KT 10SM FEW040 32/22 A2978 RMK AO2 PK WND 19035/2311 SLP070 T03170217 10339 20317 52001 $",
            observationTime = 1750549980,
            temperature = 31.7,
            dewpoint = 21.7,
            windDirection = 200,
            windSpeed = 18,
            windGust = 33,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(
                MetarCloudDto("FEW", 4000)
            ),
            wxString = null,
            latitude = 39.2975,
            longitude = -94.7309
        )

        val metar = metarDto.toMetar()

        assertEquals("Latitude should match", 39.2975, metar.latitude ?: 0.0, 0.0001)
        assertEquals("Longitude should match", -94.7309, metar.longitude ?: 0.0, 0.0001)
    }

    @Test
    fun `test latitude and longitude parsing with null values`() {
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM FEW044 FEW250 33/23 A2978 RMK AO2 PK WND 20037/2342 SLP079 T03330233 10344 20333 53002",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(
                MetarCloudDto("FEW", 4400),
                MetarCloudDto("FEW", 25000)
            ),
            wxString = null,
            latitude = null,
            longitude = null
        )

        val metar = metarDto.toMetar()

        assertNull("Latitude should be null", metar.latitude)
        assertNull("Longitude should be null", metar.longitude)
    }

    @Test
    fun `test latitude and longitude parsing with different values`() {
        val metarDto = MetarDto(
            icaoId = "KBOS",
            rawText = "KBOS 212354Z 13006KT 10SM FEW150 SCT220 BKN250 21/14 A3006 RMK AO2 SLP179 T02110144 10261 20211 53003",
            observationTime = 1750550040,
            temperature = 21.1,
            dewpoint = 14.4,
            windDirection = 130,
            windSpeed = 6,
            windGust = null,
            visibility = "10+",
            altimeter = 1018.0,
            clouds = listOf(
                MetarCloudDto("FEW", 15000),
                MetarCloudDto("SCT", 22000),
                MetarCloudDto("BKN", 25000)
            ),
            wxString = null,
            latitude = 42.3606,
            longitude = -71.0097
        )

        val metar = metarDto.toMetar()

        assertEquals("Latitude should match", 42.3606, metar.latitude ?: 0.0, 0.0001)
        assertEquals("Longitude should match", -71.0097, metar.longitude ?: 0.0, 0.0001)
    }
}