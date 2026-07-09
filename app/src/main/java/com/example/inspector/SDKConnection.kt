package com.example.inspector
import android.content.Context
import android.os.RemoteException
import android.util.Log
import com.ainirobot.coreservice.client.ApiListener
import com.ainirobot.coreservice.client.RobotApi
import com.ainirobot.coreservice.client.module.ModuleCallbackApi

class ModuleCallback : ModuleCallbackApi() {
    @Throws(RemoteException::class)
    override fun onSendRequest(
        reqId: Int,
        reqType: String?,
        reqText: String?,
        reqParam: String?
    ): Boolean {
        //receive voice command,
        //reqTyp : voice command type
        //reqText : voice to text
        //reqParam : voice command parameter
        return true
    }

    @Throws(RemoteException::class)
    override fun onRecovery() {
        //When receiving the event, regain control of the robot
        isRobotServerConnected = true
    }

    @Throws(RemoteException::class)
    override fun onSuspend() {
        //Control is deprived by the system. When receiving this event, all Api calls are invalid
        isRobotServerConnected = false
    }
}

var isRobotServerConnected = false

fun connectToRobotServer(
    context: Context,
    onConnected: () -> Unit,
    onDisconnected: () -> Unit = {},
    onDisabled: () -> Unit = {}
) {
    RobotApi.getInstance().connectServer(context, object : ApiListener {
        override fun handleApiDisabled() {
            Log.d("SDKConnection", "Robot API disabled")
            isRobotServerConnected = false
            onDisabled()
        }

        override fun handleApiConnected() {
            Log.d("SDKConnection", "Robot API connected")
            isRobotServerConnected = true
            // Server is connected, set the callback for receiving requests, including voice commands, system events, etc.
            RobotApi.getInstance().setCallback(ModuleCallback())
            onConnected()
        }

        override fun handleApiDisconnected() {
            Log.d("SDKConnection", "Robot API disconnected")
            isRobotServerConnected = false
            onDisconnected()
        }
    })
}
