package com.example.inspector

import android.os.RemoteException
import android.util.Log
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.ActionListener
import org.json.JSONObject
import com.ainirobot.coreservice.client.listener.CommandListener

var navigationListener: ActionListener = object : ActionListener() {
    @Throws(RemoteException::class)
    public override fun onResult(status: Int, response: String?) {
        Log.d("NavigationLogic", "onResult status: $status response: $response")
        when (status) {
            Definition.RESULT_OK -> if ("true" == response) {
                Log.i("NavigationLogic", "Navigation successful")
            } else {
                Log.e("NavigationLogic", "Navigation failed")
            }
        }
    }

    @Throws(RemoteException::class)
    public override fun onError(errorCode: Int, errorString: String?) {
        Log.e("NavigationLogic", "onError errorCode: $errorCode errorString: $errorString")
        when (errorCode) {
            -109 -> Log.e("NavigationLogic", "Error: Navigation Timeout (The robot took too long)")
            Definition.ERROR_NOT_ESTIMATE -> Log.e("NavigationLogic", "Error: Robot not localized (Not Estimated)")
            Definition.ERROR_IN_DESTINATION -> Log.i("NavigationLogic", "Error: Already at destination")
            Definition.ERROR_DESTINATION_NOT_EXIST -> Log.e("NavigationLogic", "Error: Destination does not exist")
            Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE -> Log.e("NavigationLogic", "Error: Cannot arrive at destination (Path blocked or narrow)")
            Definition.ACTION_RESPONSE_ALREADY_RUN -> Log.w("NavigationLogic", "Error: Navigation already running")
            Definition.ACTION_RESPONSE_REQUEST_RES_ERROR -> Log.e("NavigationLogic", "Error: Resource request error")
            Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT -> Log.e("NavigationLogic", "Error: Multi-robot waiting timeout")
            Definition.ERROR_NAVIGATION_FAILED -> Log.e("NavigationLogic", "Error: Navigation failed")
            else -> Log.e("NavigationLogic", "Unknown error code: $errorCode")
        }
    }

    public override fun onStatusUpdate(status: Int, data: String?, extraData: String?) {
        Log.d("NavigationLogic", "onStatusUpdate status: $status data: $data")
        when (status) {
            Definition.STATUS_NAVI_AVOID -> {
                RobotLogManager.addLog("NavigationLogic", "Status: Avoiding obstacle!")
                fetchAndLogPosition()
                Log.i("NavigationLogic", "Status: Avoiding obstacle")

                // Fetch robot position
                val currentReqId = reqIdCounter++
                RobotApi.getInstance().getPosition(currentReqId, object : CommandListener() {
                    override fun onResult(result: Int, message: String?) {
                        try {
                            val json = JSONObject(message)
                            val x = json.getDouble(Definition.JSON_NAVI_POSITION_X)
                            val y = json.getDouble(Definition.JSON_NAVI_POSITION_Y)
                            val theta = json.getDouble(Definition.JSON_NAVI_POSITION_THETA)

                            // Log obstacle
                            ObstacleManager.addObstacle(x, y, theta)
                            Log.d("NavigationLogic", "Obstacle logged at x=$x y=$y theta=$theta")

                        } catch (e: Exception) {
                            Log.e("NavigationLogic", "Error parsing position: ${e.message}")
                        }
                    }
                })
            }

            Definition.STATUS_NAVI_AVOID_END -> Log.i("NavigationLogic", "Status: Obstacle avoidance ended")
            Definition.STATUS_START_NAVIGATION -> {
                Log.i("NavigationLogic", "Status: Navigation started")
                ObstacleManager.startPoseListener()   // ← MOVE IT HERE
            }
            Definition.STATUS_START_CRUISE -> Log.i("NavigationLogic", "Status: Cruise started")
            Definition.STATUS_NAVI_OUT_MAP -> Log.w("NavigationLogic", "Status: Robot out of map")
            Definition.STATUS_NAVI_MULTI_ROBOT_WAITING -> Log.i("NavigationLogic", "Status: Waiting for other robot")
            Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END -> Log.i("NavigationLogic", "Status: Finished waiting for other robot")
            Definition.STATUS_NAVI_GO_STRAIGHT -> Log.d("NavigationLogic", "Status: Going straight")
            Definition.STATUS_NAVI_TURN_LEFT -> Log.d("NavigationLogic", "Status: Turning left")
            Definition.STATUS_NAVI_TURN_RIGHT -> Log.d("NavigationLogic", "Status: Turning right")
        }
    }
}

private fun fetchAndLogPosition() {
    val currentReqId = reqIdCounter++
    RobotApi.getInstance().getPosition(currentReqId, object : CommandListener() {
        override fun onResult(result: Int, message: String?) {
            try {
                val json = JSONObject(message)
                val x = json.optDouble(Definition.JSON_NAVI_POSITION_X)
                val y = json.optDouble(Definition.JSON_NAVI_POSITION_Y)

                val posMsg = "Current Position: x=$x, y=$y"
                RobotLogManager.addLog("NavigationLogic", posMsg)

                // Also update your ObstacleManager
                val theta = json.optDouble(Definition.JSON_NAVI_POSITION_THETA)
                ObstacleManager.addObstacle(x, y, theta)
            } catch (e: Exception) {
                RobotLogManager.addLog("NavigationLogic", "Position Error: ${e.message}")
            }
        }
    })
}

var reqIdCounter = 0
var coordinateDeviation = 1.0
var obstacleDistance = 0.3  // Reduced from 0.75 to allow narrow passages
var time = 60000L             // Increased from 60L to prevent early timeout
var linearSpeed = 0.7
var angularSpeed = 1.2

fun customNavigationObstacleDistance(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Custom navigation with obstacle distance to '$destName' reqId=$currentReqId connected=$isRobotServerConnected")

    RobotApi.getInstance().startNavigation(currentReqId,
        destName,
        coordinateDeviation,
        obstacleDistance,
        time,
        linearSpeed,
        angularSpeed,
        navigationListener
    );
}

fun stopNavigation(){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "stop navigating reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().stopNavigation(currentReqId);
}