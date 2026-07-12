package com.example.inspector

import android.util.Log
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.CommandListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object MapManager {
    fun getPlaceList() {
        var currentReqId = reqIdCounter++
        Log.d("MapManager", "Returned places reqId=$currentReqId connected=$isRobotServerConnected")
        RobotApi.getInstance().getPlaceList(currentReqId, object : CommandListener() {
            override fun onResult(result: Int, message: String?) {
                try {
                    val jsonArray = JSONArray(message)
                    val length = jsonArray.length()
//                    for (i in 0..<length) {
//                        val json = jsonArray.getJSONObject(i)
//                        json.getDouble("x")
//
//                        //x coordinate
//                        json.getDouble("y")
//
//                        //y coordinate
//                        json.getDouble("theta")
//
//                        //z coordinate
//                        json.getString("name")
//
//                        //position name
//                    }

                    for (i in 0 until length) {

                        val json = jsonArray.getJSONObject(i)

                        val x = json.getDouble("x")
                        val y = json.getDouble("y")
                        val theta = json.getDouble("theta")
                        val name = json.getString("name")

                        Log.d(
                            "MapManager",
                            "Place: $name  x=$x  y=$y  theta=$theta"
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getPosition() {
        var currentReqId = reqIdCounter++
        Log.d(
            "MapManager",
            "Returned Cords on map reqId=$currentReqId connected=$isRobotServerConnected"
        )
        RobotApi.getInstance().getPosition(currentReqId, object : CommandListener() {
            override fun onResult(result: Int, message: String) {
                try {
                    val json = JSONObject(message)

                    //x coordinate
                    val x = json.getDouble(Definition.JSON_NAVI_POSITION_X)

                    //y coordinate
                    val y = json.getDouble(Definition.JSON_NAVI_POSITION_Y)

                    //z coordinate
                    val z = json.getDouble(Definition.JSON_NAVI_POSITION_THETA)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: java.lang.NullPointerException) {
                    e.printStackTrace()
                }
            }
        })
    }
}