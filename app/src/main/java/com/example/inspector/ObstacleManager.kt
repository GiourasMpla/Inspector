package com.example.inspector

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class ObstacleEntry(
    val x: Double,
    val y: Double,
    val theta: Double,
    val timestamp: Long
)

object ObstacleManager {
    var obstacles by mutableStateOf<List<ObstacleEntry>>(emptyList())

    fun addObstacle(x: Double, y: Double, theta: Double) {
        val entry = ObstacleEntry(x, y, theta, System.currentTimeMillis())
        obstacles = obstacles + entry
    }

    fun clear() {
        obstacles = emptyList()
    }
}
