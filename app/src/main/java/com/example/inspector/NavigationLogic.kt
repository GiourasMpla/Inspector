package com.example.inspector

import android.os.RemoteException
import android.util.Log
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.ActionListener


var navigationListener: ActionListener = object : ActionListener() {
    @Throws(RemoteException::class)
    public override fun onResult(status: Int, response: String?) {
        when (status) {
            Definition.RESULT_OK -> if ("true" == response) {
                //navigation is successful
            } else {
                //navigation is failed
            }
        }
    }

    @Throws(RemoteException::class)
    public override fun onError(errorCode: Int, errorString: String?) {
        when (errorCode) {
            Definition.ERROR_NOT_ESTIMATE -> {}
            Definition.ERROR_IN_DESTINATION -> {}
            Definition.ERROR_DESTINATION_NOT_EXIST -> {}
            Definition.ERROR_DESTINATION_CAN_NOT_ARRAIVE -> {}
            Definition.ACTION_RESPONSE_ALREADY_RUN -> {}
            Definition.ACTION_RESPONSE_REQUEST_RES_ERROR -> {}
            Definition.ERROR_MULTI_ROBOT_WAITING_TIMEOUT -> {}
            Definition.ERROR_NAVIGATION_FAILED -> {}
        }
    }

    public override fun onStatusUpdate(status: Int, data: String?, extraData: String?) {
        when (status) {
            Definition.STATUS_NAVI_AVOID -> {}
            Definition.STATUS_NAVI_AVOID_END -> {}
            Definition.STATUS_START_NAVIGATION -> {}
            Definition.STATUS_START_CRUISE -> {}
            Definition.STATUS_NAVI_OUT_MAP -> {}
            Definition.STATUS_NAVI_MULTI_ROBOT_WAITING -> {}
            Definition.STATUS_NAVI_MULTI_ROBOT_WAITING_END -> {}
            Definition.STATUS_NAVI_GO_STRAIGHT -> {}
            Definition.STATUS_NAVI_TURN_LEFT -> {}
            Definition.STATUS_NAVI_TURN_RIGHT -> {}
        }
    }
}

var reqIdCounter = 0
var coordinateDeviation = 0.5 //we put 0.5f because a warehouse is a big space so we don't care much
var obstacleDistance = 0.75 //how far can the robot see an obstacle before it stops
var time = 60L//how much time can the robot wait at an obstacle without moving 0.1m else the navigation fails
var linearSpeed = 0.7 //0.7 m/s is the default
var angularSpeed = 0.45 //must follow this law: angularSpeed = 0.4 + (linearSpeed-0.1) / 3 * 4
//default navigation speed
fun normalNavigate(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Normal navigation reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, time, navigationListener);
}

//Default navigation speed, specify obstacle avoidance distance
fun navigateObstacleDistance(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Normal navigation reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, obstacleDistance, time, navigationListener);
}

//custom navigation speed linear and angular
fun customNavigation(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Normal navigation reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, time, linearSpeed, angularSpeed, navigationListener);
}

//custom navigation speed linear and angular, specify obstacle avoidance distance
fun customNavigationObstacleDistance(destName: String){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "Normal navigation reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().startNavigation(currentReqId, destName, coordinateDeviation, obstacleDistance, time, linearSpeed, angularSpeed, navigationListener);
}

//stops navigation
fun stopNavigation(){
    var currentReqId = reqIdCounter++
    Log.d("MoveLogic", "stop navigating reqId=$currentReqId connected=$isRobotServerConnected")
    RobotApi.getInstance().stopNavigation(currentReqId);
}