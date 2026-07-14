package com.example.inspector

import android.os.RemoteException
import android.util.Log
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.ActionListener


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
            Definition.STATUS_NAVI_AVOID -> Log.i("NavigationLogic", "Status: Avoiding obstacle")
            Definition.STATUS_NAVI_AVOID_END -> Log.i("NavigationLogic", "Status: Obstacle avoidance ended")
            Definition.STATUS_START_NAVIGATION -> Log.i("NavigationLogic", "Status: Navigation started")
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

var reqIdCounter = 0
var coordinateDeviation = 0.5 
var obstacleDistance = 0.3  // Reduced from 0.75 to allow narrow passages
var time = 1200L             // Increased from 60L to prevent early timeout
var linearSpeed = 0.7 
var angularSpeed = 0.45 

// Updated normalNavigate to use obstacleDistance and the new timeout
fun normalNavigate(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Normal navigation to '$destName' reqId=$currentReqId connected=$isRobotServerConnected")
    // Passing obstacleDistance explicitly helps the robot navigate tighter spaces
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, time, navigationListener);
}

fun navigateObstacleDistance(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Navigation with obstacle distance to '$destName' reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, obstacleDistance, time, navigationListener);
}

fun customNavigation(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Custom navigation to '$destName' reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, navigationListener);
}

fun customNavigationObstacleDistance(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Custom navigation with obstacle distance to '$destName' reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, obstacleDistance, time, linearSpeed, angularSpeed, navigationListener);
}

fun stopNavigation(){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "stop navigating reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().stopNavigation(currentReqId);
}