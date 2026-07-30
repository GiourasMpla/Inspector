package com.example.inspector

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.StatusListener
import org.json.JSONObject
import kotlin.math.sqrt


data class ObstacleEntry(
    val x: Double,
    val y: Double,
    val theta: Double,
    val timestamp: Long
)

data class SavedLocation(
    val name: String,
    val x: Double,
    val y: Double
)

object ObstacleManager {
    var obstacles by mutableStateOf<List<ObstacleEntry>>(emptyList())

    var savedLocations: List<SavedLocation> = emptyList()

    fun updateSavedLocations(list: List<SavedLocation>) {
        savedLocations = list
    }

    fun addObstacle(x: Double, y: Double, theta: Double) {
        val entry = ObstacleEntry(x, y, theta, System.currentTimeMillis())
        obstacles = obstacles + entry
        val near = nearestLocation(x, y)
        val logMsg = "Obstacle near $near (x=$x, y=$y)"

        RobotLogManager.addLog("ObstacleManager", logMsg)
        MqttManager.publishObstacle(
            entry,
            near = near
        )
    }

    fun clear() {
        obstacles = emptyList()
    }

    fun nearestLocation(x: Double, y: Double): String {
        if (savedLocations.isEmpty()) return "Unknown"

        var bestName = "Unknown"
        var bestDist = Double.MAX_VALUE

        for (loc in savedLocations) {
            val dx = x - loc.x
            val dy = y - loc.y
            val dist = sqrt(dx*dx + dy*dy)

            if (dist < bestDist) {
                bestDist = dist
                bestName = loc.name
            }
        }

        return bestName
    }
}
