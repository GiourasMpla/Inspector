package com.example.inspector

import android.util.Log
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import org.json.JSONObject
import kotlin.concurrent.thread

object MqttManager {

    private const val BROKER_IP = "broker.hivemq.com"
    private const val TOPIC = "robot/obstacles"

    private val client = MqttClient.builder()
        .useMqttVersion3()
        .serverHost(BROKER_IP)
        .serverPort(1883)
        .identifier("nova_robot_client")
        .buildAsync()

    fun connect() {
        thread(isDaemon = true, name = "MQTT-Connect") {
            try {
                client.connect()
                    .whenComplete { _, error ->
                        if (error != null) {
                            Log.e("MqttManager", "Failed to connect to MQTT: ${error.message}")
                        } else {
                            Log.d("MqttManager", "Connected to MQTT broker")
                        }
                    }
            } catch (e: Exception) {
                Log.e("MqttManager", "MQTT connect error: ${e.message}")
            }
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

            client.publishWith()
                .topic(TOPIC)
                .qos(MqttQos.AT_MOST_ONCE)
                .payload(json.toString().toByteArray())
                .send()
                .whenComplete { _, error ->
                    if (error != null) {
                        Log.e("MqttManager", "MQTT publish error: ${error.message}")
                    } else {
                        Log.d("MqttManager", "Published obstacle: $json")
                    }
                }

        } catch (e: Exception) {
            Log.e("MqttManager", "MQTT publish error: ${e.message}")
        }
    }
}
