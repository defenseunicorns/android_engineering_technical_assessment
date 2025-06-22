package com.defenseunicorns.flyaware.data.remote.dto

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.ZoneOffset

fun String.toZonedDateTime(): ZonedDateTime {
    return try {
        // Parse the specific format "2023-11-03 23:20:00"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val localDateTime = LocalDateTime.parse(this, formatter)
        localDateTime.atZone(ZoneOffset.UTC)
    } catch (e: Exception) {
        // If parsing fails, return current time
        ZonedDateTime.now()
    }
}