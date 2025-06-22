package com.defenseunicorns.flyaware

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.MetarCloudDto
import com.defenseunicorns.flyaware.model.WeatherIntensity
import org.junit.Test
import org.junit.Assert.*

class MetarWeatherConditionsTest {
    
    @Test
    fun `test weather conditions parsing from raw METAR with current rain`() {
        // Test case with actual current weather conditions
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM RA FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(
                MetarCloudDto("FEW", 4400)
            ),
            wxString = null
        )
        
        val metar = metarDto.toMetar()
        
        // Should parse RA as current rain
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be moderate", WeatherIntensity.MODERATE, condition.intensity)
        assertTrue("Should contain rain", condition.phenomena.contains("Rain"))
    }
    
    @Test
    fun `test weather conditions parsing from wxString`() {
        // Test case with wxString field populated
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
            wxString = "+RA" // Heavy rain
        )
        
        val metar = metarDto.toMetar()
        
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be heavy", WeatherIntensity.HEAVY, condition.intensity)
        assertTrue("Should contain rain", condition.phenomena.contains("Rain"))
    }
    
    @Test
    fun `test weather conditions parsing from raw METAR with multiple phenomena`() {
        // Test case with multiple weather phenomena
        val metarDto = MetarDto(
            icaoId = "KMDW",
            rawText = "KMDW 212351Z 21016G37KT 10SM -RASN FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(
                MetarCloudDto("FEW", 4400)
            ),
            wxString = null
        )
        
        val metar = metarDto.toMetar()
        
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be light", WeatherIntensity.LIGHT, condition.intensity)
        assertTrue("Should contain rain", condition.phenomena.contains("Rain"))
        assertTrue("Should contain snow", condition.phenomena.contains("Snow"))
    }
    
    @Test
    fun `test weather conditions parsing with no weather`() {
        // Test case with no weather conditions (clear weather)
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
            wxString = null
        )
        
        val metar = metarDto.toMetar()
        
        // Should have no weather conditions for clear weather
        assertTrue("Weather conditions should be empty", metar.weatherConditions.isEmpty())
    }
    
    @Test
    fun `test weather conditions parsing with vicinity weather`() {
        // Test case with vicinity weather (VC)
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM VCTS FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(
                MetarCloudDto("FEW", 4400)
            ),
            wxString = null
        )
        
        val metar = metarDto.toMetar()
        
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be vicinity", WeatherIntensity.VICINITY, condition.intensity)
        assertTrue("Should contain thunderstorm", condition.phenomena.contains("Thunderstorm"))
    }

    @Test
    fun `test snow parsing from raw METAR`() {
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM -SN FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(MetarCloudDto("FEW", 4400)),
            wxString = null
        )

        val metar = metarDto.toMetar()

        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)

        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be light", WeatherIntensity.LIGHT, condition.intensity)
        assertTrue("Should contain snow", condition.phenomena.contains("Snow"))
        assertEquals("Should have 1 phenomenon", 1, condition.phenomena.size)
    }

    @Test
    fun `test vicinity thunderstorm parsing`() {
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM VCTS FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(MetarCloudDto("FEW", 4400)),
            wxString = null
        )

        val metar = metarDto.toMetar()

        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)

        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be vicinity", WeatherIntensity.VICINITY, condition.intensity)
        assertTrue("Should contain thunderstorm", condition.phenomena.contains("Thunderstorm"))
        assertEquals("Should have 1 phenomenon", 1, condition.phenomena.size)
    }
} 