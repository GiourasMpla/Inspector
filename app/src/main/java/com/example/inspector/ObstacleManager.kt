package com.example.inspector

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.StatusListener
import com.ainirobot.coreservice.client.actionbean.Pose
import org.json.JSONObject


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

    fun startPoseListener() {
        RobotApi.getInstance().registerStatusListener(
            Definition.STATUS_POSE,
            object : StatusListener() {
                override fun onStatusUpdate(type: String, value: String) {
                    try {
                        val json = JSONObject(value)
                        val px = json.optDouble("px")
                        val py = json.optDouble("py")
                        val theta = json.optDouble("theta")
                        val status = json.optInt("status")


                        // OBSTACLE = 2
                        if (status == 2) {
                            addObstacle(px, py, theta)
                            RobotLogManager.addLog(
                                "PoseListener",
                                "Obstacle detected at x=$px, y=$py"
                            )
                        }

                    } catch (e: Exception) {
                        Log.e("PoseListener", "Pose parse error: ${e.message}")
                    }
                }
            }
        )
    }
}