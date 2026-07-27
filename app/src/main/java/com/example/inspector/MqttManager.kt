package com.example.inspector

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject

object MqttManager {

    private const val BROKER_URL = "tcp://192.168.1.60:1883"
    private const val TOPIC = "robot/obstacles"

    private var client: MqttAndroidClient? = null

    fun connect(context: Context) {
        if (client != null && client!!.isConnected) return

        client = MqttAndroidClient(context, BROKER_URL, "nova_robot_client")

        try {
            val token: IMqttToken = client!!.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MqttManager", "Connected to MQTT broker")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MqttManager", "Failed to connect to MQTT: ${exception?.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("MqttManager", "MQTT connect error: ${e.message}")
        }
    }

    fun publishObstacle(entry: ObstacleEntry, near: String) {
        try {
            val json = JSONObject().apply {
                put("x", entry.x)
                put("y", entry.y)
                put("theta", entry.theta)
                put("timestamp", entry.timestamp)
                put("near", near)
            }

            val message = MqttMessage(json.toString().toByteArray())
            message.qos = 0

            client?.publish(TOPIC, message)
            Log.d("MqttManager", "Published obstacle: $json")

        } catch (e: Exception) {
            Log.e("MqttManager", "MQTT publish error: ${e.message}")
        }
    }
}
