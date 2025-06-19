package com.defenseunicorns.flyaware.airportlist

data class AirportListState(
    val airports: List<AirportItem> = emptyList()
)

data class AirportItem(
    val name: String = ""
)