/*
 * Copyright 2024 Defense Unicorns
 * SPDX-License-Identifier: LicenseRef-Defense-Unicorns-Commercial
 */
package com.defenseunicorns.flyaware.model

/**
 * Domain model class representing a PIREP (Pilot Report)
 */
data class Pirep(
    val reportId: String,
    val aircraftType: String,
    val observationTime: String,
    val location: String,
    val altitude: Int?,
    val rawText: String,
    val weatherString: String?,
    val skyCondition: String?,
    val turbulence: String?,
    val icing: String?,
    val visibility: Double?,
    val temperature: Int?
)
