package com.example.inspector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inspector.ui.screens.LogsScreen
import com.example.inspector.ui.screens.NavigationSelectionScreen
import com.example.inspector.ui.screens.NavigationStatusScreen
import com.example.inspector.ui.screens.StartingScreen
import com.example.inspector.ui.theme.InspectorTheme

class MainActivity : ComponentActivity() {
    private var robotConnected by mutableStateOf(false)
    private var sdkStatus by mutableStateOf("Connecting to robot service...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        connectToRobotServer(
            context = this,
            onConnected = {
                robotConnected = true
                sdkStatus = "Robot API connected"

                // Προσωρινό test
                MapManager.getPlaceList()
            },
            onDisconnected = {
                robotConnected = false
                sdkStatus = "Robot API disconnected"
            },
            onDisabled = {
                robotConnected = false
                sdkStatus = "Robot API disabled"
            }
        )
        setContent {
            InspectorTheme {
                ScreenMain()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenMain(

){

    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = Routes.StartingScreen.route){
        composable(Routes.StartingScreen.route){
            StartingScreen(navController = navController)
        }

        composable(Routes.NavigationSelectionScreen.route){
            NavigationSelectionScreen(navController = navController)
        }

        composable(Routes.NavigationStatusScreen.route){
            NavigationStatusScreen(navController = navController)
        }

        composable(Routes.LogsScreen.route){
            LogsScreen(navController = navController)
        }
    }
}