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
        Log.d("PoseListener", "startPoseListener() called")
        RobotApi.getInstance().registerStatusListener(
            Definition.STATUS_POSE,
            object : StatusListener() {
                override fun onStatusUpdate(type: String, value: String) {
                    Log.d("PoseListener", "onStatusUpdate() fired. type=$type value=$value")
                    try {
                        val json = JSONObject(value)
                        val px = json.optDouble("px")
                        val py = json.optDouble("py")
                        val theta = json.optDouble("theta")
                        val status = json.optInt("status")

                        Log.d(
                            "PoseListener",
                            "Parsed pose: px=$px py=$py theta=$theta status=$status"
                        )


                        // OBSTACLE = 2
                        if (status == 2) {
                            Log.d("PoseListener", "Status==2, adding obstacle")
                            addObstacle(px, py, theta)
                            RobotLogManager.addLog(
                                "PoseListener",
                                "Obstacle detected at x=$px, y=$py"
                            )
                        }else{
                            Log.d("PoseListener", "Status != 2, ignoring")
                        }

                    } catch (e: Exception) {
                        Log.e("PoseListener", "Pose parse error: ${e.message}")
                    }
                }
            }
        )
        Log.d("PoseListener", "registerStatusListener(STATUS_POSE) called with STATUS_POSE=${Definition.STATUS_POSE}")
    }
}
