package com.example.inspector.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inspector.Routes
import com.example.inspector.stopNavigation

@Composable
fun NavigationStatusScreen(
    robotConnected: Boolean = true,
    sdkStatus: String = "Preview",
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { stopNavigation() },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Stop navigation")
        }
        Text(//to be updated when the robot navigates
            text = "Navigating to ..."
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = { navController.navigate(Routes.StartingScreen.route) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Return to Starting screen")
        }
    }
}