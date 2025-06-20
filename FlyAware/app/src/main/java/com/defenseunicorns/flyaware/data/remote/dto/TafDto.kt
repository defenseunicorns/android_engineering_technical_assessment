package com.defenseunicorns.flyaware.data.remote.dto

import com.defenseunicorns.flyaware.model.Taf
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class TafDto(
    val station_id: String,
    val raw_text: String,
    val issue_time: String,
    val validFromTime: String,
    val validToTime: String,
//    val forecast: List<TafForecastDto>
) {
    fun toTaf() : Taf {
        return Taf(
            rawText = raw_text,
            icaoCode = station_id,
            issueTime = try { issue_time.toZonedDateTime() } catch (e: Exception) { ZonedDateTime.now() },
            validFromTime = try { validFromTime.toZonedDateTime() } catch (e: Exception) { ZonedDateTime.now() },
            validUntilTime = try { validToTime.toZonedDateTime() } catch (e: Exception) { ZonedDateTime.now() },
//            forecastPeriods = TODO(),
        )
    }
}
//
//@Serializable
//data class TafForecastDto(
//    val fcst_time_from: String,
//    val fcst_time_to: String,
//    val wind_dir_degrees: Int?,
//    val wind_speed_kt: Int?,
//    val visibility_statute_mi: Float?,
//    val sky_condition: List<SkyConditionDto>?
//)
//
//@Serializable
//data class SkyConditionDto(
//    val intensity: WeatherIntensity? = null,
//    val descriptor: String? = null,
//    val phenomena: List<String> = emptyList()
//)
