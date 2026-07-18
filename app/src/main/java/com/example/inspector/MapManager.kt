package com.example.inspector

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ainirobot.coreservice.client.Definition
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.listener.CommandListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

data class MapPlace(
    val name: String,
    val x: Double,
    val y: Double,
    val theta: Double
)

object MapManager {
    var places by mutableStateOf<List<MapPlace>>(emptyList())

    fun getPlaceList() {
        var currentReqId = reqIdCounter++
        Log.d("MapManager", "Fetching places reqId=$currentReqId connected=$isRobotServerConnected")
        RobotApi.getInstance().getPlaceList(currentReqId, object : CommandListener() {
            override fun onResult(result: Int, message: String?) {
                try {
                    val jsonArray = JSONArray(message)
                    val length = jsonArray.length()
                    val placesList = mutableListOf<MapPlace>()

                    for (i in 0 until length) {
                        val json = jsonArray.getJSONObject(i)
                        val x = json.getDouble("x")
                        val y = json.getDouble("y")
                        val theta = json.getDouble("theta")
                        val name = json.getString("name")

                        placesList.add(MapPlace(name, x, y, theta))
                        Log.d("MapManager", "Place $name: x=$x, y=$y, theta=$theta")
                    }

                    // Store the places in state for UI access
                    places = placesList
                    Log.d("MapManager", "Successfully loaded ${placesList.size} places")
                } catch (e: JSONException) {
                    Log.e("MapManager", "JSON parsing error: ${e.message}")
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    Log.e("MapManager", "Null pointer error: ${e.message}")
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