package com.defenseunicorns.flyaware

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.MetarCloudDto
import com.defenseunicorns.flyaware.model.Metar
import com.defenseunicorns.flyaware.model.FlightCategory
import com.defenseunicorns.flyaware.model.WeatherCondition
import com.defenseunicorns.flyaware.model.WeatherIntensity
import org.junit.Test
import org.junit.Assert.*
import java.time.ZonedDateTime

class CompilationTest {
    
    @Test
    fun `test all classes can be imported and used`() {
        // Test MetarDto
        val metarDto = MetarDto(
            icaoId = "KMCI",
            rawText = "KMCI 212353Z 20018G33KT 10SM FEW040 32/22 A2978 RMK AO2",
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
        
        // Test Metar
        val metar = metarDto.toMetar()
        
        // Test FlightCategory
        val flightCategory = FlightCategory.VFR
        
        // Test WeatherCondition
        val weatherCondition = WeatherCondition(
            intensity = WeatherIntensity.MODERATE,
            descriptor = null,
            phenomena = listOf("Rain")
        )
        
        // Test WeatherIntensity
        val intensity = WeatherIntensity.HEAVY
        
        // Verify everything works
        assertNotNull("MetarDto should not be null", metarDto)
        assertNotNull("Metar should not be null", metar)
        assertNotNull("FlightCategory should not be null", flightCategory)
        assertNotNull("WeatherCondition should not be null", weatherCondition)
        assertNotNull("WeatherIntensity should not be null", intensity)
        
        assertEquals("ICAO code should match", "KMCI", metar.icaoCode)
        assertEquals("Flight category should be VFR", FlightCategory.VFR, flightCategory)
        assertEquals("Weather intensity should be HEAVY", WeatherIntensity.HEAVY, intensity)
    }
} 