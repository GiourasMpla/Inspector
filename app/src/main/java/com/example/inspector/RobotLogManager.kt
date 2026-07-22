package com.example.inspector

import android.util.Log
import androidx.compose.foundation.layout.add
import androidx.compose.runtime.mutableStateListOf

object RobotLogManager {
    // Use a StateFlow or mutableStateListOf so Compose can observe it
    val logs = mutableStateListOf<String>()

    fun addLog(tag: String, message: String) {
        val fullMessage = "[$tag] $message"
        Log.d(tag, message)
        logs.add(0, fullMessage) // Add to top of list
    }
}