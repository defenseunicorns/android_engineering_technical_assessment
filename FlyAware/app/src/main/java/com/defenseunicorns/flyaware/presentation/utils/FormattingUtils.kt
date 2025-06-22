package com.defenseunicorns.flyaware.presentation.utils

fun formatWind(direction: Int?, speed: Int?, gust: Int?): String {
    return when {
        direction != null && speed != null -> {
            val gustStr = if (gust != null) "G${gust}" else ""
            "${direction}° ${speed}kt$gustStr"
        }
        speed != null -> "${speed}kt"
        else -> "--"
    }
}