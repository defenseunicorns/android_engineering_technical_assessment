package com.defenseunicorns.flyaware

import com.defenseunicorns.flyaware.model.FlightCategory
import com.defenseunicorns.flyaware.model.Metar
import org.junit.Test
import org.junit.Assert.*
import java.time.ZonedDateTime

class WeatherInfoCardTest {
    
    @Test
    fun `test WeatherInfoCard can be created with valid Metar`() {
        val metar = Metar(
            icaoCode = "KMCI",
            rawText = "KMCI 212353Z 20018G33KT 10SM FEW040 32/22 A2978 RMK AO2",
            observationTime = ZonedDateTime.now(),
            temperature = 31.7,
            dewpoint = 21.7,
            windDirection = 200,
            windSpeed = 18,
            windGust = 33,
            visibility = 10.0,
            altimeter = 1008.5,
            flightCategory = FlightCategory.VFR,
            cloudLayers = emptyList(),
            weatherConditions = emptyList(),
            latitude = 39.2975,
            longitude = -94.7309
        )
        
        // Test that the metar has the expected values
        assertEquals("ICAO code should match", "KMCI", metar.icaoCode)
        assertEquals("Temperature should match", 31.7, metar.temperature ?: 0.0, 0.001)
        assertEquals("Latitude should match", 39.2975, metar.latitude ?: 0.0, 0.001)
        assertEquals("Longitude should match", -94.7309, metar.longitude ?: 0.0, 0.001)
        assertEquals("Flight category should match", FlightCategory.VFR, metar.flightCategory)
    }
    
    @Test
    fun `test WeatherInfoCard can be created with weather conditions`() {
        val metar = Metar(
            icaoCode = "KPDX",
            rawText = "KPDX 220021Z 20009KT 8SM FEW017 BKN031 OVC038 13/12 A3005 RMK AO2 RAE13 P0001 T01330122 $",
            observationTime = ZonedDateTime.now(),
            temperature = 13.3,
            dewpoint = 12.2,
            windDirection = 200,
            windSpeed = 9,
            windGust = null,
            visibility = 8.0,
            altimeter = 1017.7,
            flightCategory = FlightCategory.MVFR,
            cloudLayers = emptyList(),
            weatherConditions = listOf(
                com.defenseunicorns.flyaware.model.WeatherCondition(
                    intensity = com.defenseunicorns.flyaware.model.WeatherIntensity.MODERATE,
                    descriptor = null,
                    phenomena = listOf("Rain")
                )
            ),
            latitude = 45.5958,
            longitude = -122.609
        )
        
        // Test that the metar has weather conditions
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        assertEquals("Flight category should match", FlightCategory.MVFR, metar.flightCategory)
    }
} 