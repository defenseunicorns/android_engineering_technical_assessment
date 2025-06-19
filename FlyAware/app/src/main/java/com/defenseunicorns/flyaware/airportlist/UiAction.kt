package com.defenseunicorns.flyaware.airportlist

sealed interface UiAction {
    data class Add(val id: String) : UiAction
}
