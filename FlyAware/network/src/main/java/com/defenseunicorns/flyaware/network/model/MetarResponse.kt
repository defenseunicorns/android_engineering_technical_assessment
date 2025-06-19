package com.defenseunicorns.flyaware.network.model

import com.defenseunicorns.flyaware.core.model.FlightCategory
import com.defenseunicorns.flyaware.core.model.Metar
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Example response:
 * [
 *   {
 *     "metar_id":779635829,
 *     "icaoId":"KMCI",
 *     "receiptTime":"2025-06-19 20:56:35",
 *     "obsTime":1750366380,
 *     "reportTime":"2025-06-19 21:00:00",
 *     "temp":30,
 *     "dewp":17.2,
 *     "wdir":"VRB",
 *     "wspd":3,
 *     "wgst":null,
 *     "visib":"10+",
 *     "altim":1016.3,
 *     "slp":1015.3,
 *     "qcField":12,
 *     "wxString":null,
 *     "presTend":-1.2,
 *     "maxT":null,
 *     "minT":null,
 *     "maxT24":null,
 *     "minT24":null,
 *     "precip":null,
 *     "pcp3hr":null,
 *     "pcp6hr":null,
 *     "pcp24hr":null,
 *     "snow":null,
 *     "vertVis":null,
 *     "metarType":"METAR",
 *     "rawOb":"KMCI 192053Z VRB03KT 10SM FEW030 30/17 A3001 RMK AO2 SLP153 T03000172 56012 $",
 *     "mostRecent":1,
 *     "lat":39.2975,
 *     "lon":-94.7309,
 *     "elev":308,
 *     "prior":1,
 *     "name":"Kansas City Intl, MO, US",
 *     "clouds":[
 *       {
 *         "cover":"FEW",
 *         "base":3000
 *       }
 *     ],
 *     "rawTaf":"KMCI 191730Z 1918/2018 VRB03KT P6SM SKC FM192200 19007KT P6SM SKC FM200600 18012KT P6SM SKC FM201400 19014G20KT P6SM SCT250"
 *   }
 * ]
 */
@Serializable
data class MetarResponse(
    val icaoId: String,
    val rawOb: String,
    val obsTime: Long,
    val temp: Double?,
    val dewp: Double?,
    // val wdir: Int?, // Leaving wind direction out for now; not sure how to convert it.
    val wspd: Int?,
    val wgst: Int?,
    val visib: String,
    val altim: Double?
) {
    fun asCoreModel() = Metar(
        icaoCode = icaoId,
        rawText = rawOb,
        observationTime = ZonedDateTime.ofInstant(
            Instant.ofEpochMilli(obsTime),
            ZoneId.systemDefault()
        ),
        temperature = temp,
        dewpoint = dewp,
        windDirection = null,
        windSpeed = wspd,
        windGust = wgst,
        visibility = visib,
        altimeter = altim,
        flightCategory = FlightCategory.UNKNOWN, // Unsure how to find this.
        cloudLayers = emptyList(), // TODO
        weatherConditions = emptyList() // Unsure how to find this.
    )
}