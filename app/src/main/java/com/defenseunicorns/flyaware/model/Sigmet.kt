/*
 * Copyright 2024 Defense Unicorns
 * SPDX-License-Identifier: LicenseRef-Defense-Unicorns-Commercial
 */
package com.defenseunicorns.flyaware.model

/**
 * Domain model class representing a SIGMET (Significant Meteorological Information)
 */
data class Sigmet(
    val id: String,
    val area: String,
    val validFromTime: String,
    val validToTime: String,
    val rawText: String,
    val hazard: String,
    val severity: String,
    val movement: String = "Unknown"
)
