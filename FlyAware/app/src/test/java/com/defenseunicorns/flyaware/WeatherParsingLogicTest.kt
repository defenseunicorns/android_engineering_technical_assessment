package com.defenseunicorns.flyaware

import com.defenseunicorns.flyaware.data.remote.dto.MetarDto
import com.defenseunicorns.flyaware.data.remote.dto.MetarCloudDto
import com.defenseunicorns.flyaware.model.WeatherIntensity
import org.junit.Test
import org.junit.Assert.*

class WeatherParsingLogicTest {
    
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
    fun `test combined rain and snow parsing`() {
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM RASN FEW044 33/23 A2978 RMK AO2",
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
        assertEquals("Intensity should be moderate", WeatherIntensity.MODERATE, condition.intensity)
        assertTrue("Should contain rain", condition.phenomena.contains("Rain"))
        assertTrue("Should contain snow", condition.phenomena.contains("Snow"))
        assertEquals("Should have 2 phenomena", 2, condition.phenomena.size)
    }
    
    @Test
    fun `test light rain and snow parsing`() {
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
            clouds = listOf(MetarCloudDto("FEW", 4400)),
            wxString = null
        )
        
        val metar = metarDto.toMetar()
        
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be light", WeatherIntensity.LIGHT, condition.intensity)
        assertTrue("Should contain rain", condition.phenomena.contains("Rain"))
        assertTrue("Should contain snow", condition.phenomena.contains("Snow"))
        assertEquals("Should have 2 phenomena", 2, condition.phenomena.size)
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
    
    @Test
    fun `test wxString parsing with snow`() {
        val metarDto = MetarDto(
            icaoId = "KORD",
            rawText = "KORD 212351Z 21016G37KT 10SM FEW044 33/23 A2978 RMK AO2",
            observationTime = 1750549860,
            temperature = 33.3,
            dewpoint = 23.3,
            windDirection = 210,
            windSpeed = 16,
            windGust = 37,
            visibility = "10+",
            altimeter = 1008.5,
            clouds = listOf(MetarCloudDto("FEW", 4400)),
            wxString = "-SN" // Light snow
        )
        
        val metar = metarDto.toMetar()
        
        assertTrue("Weather conditions should be present", metar.weatherConditions.isNotEmpty())
        assertEquals("Should have 1 weather condition", 1, metar.weatherConditions.size)
        
        val condition = metar.weatherConditions.first()
        assertEquals("Intensity should be light", WeatherIntensity.LIGHT, condition.intensity)
        assertTrue("Should contain snow", condition.phenomena.contains("Snow"))
        assertEquals("Should have 1 phenomenon", 1, condition.phenomena.size)
    }
} 