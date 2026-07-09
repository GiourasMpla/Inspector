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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inspector.Routes

@Composable
fun StartingScreen(
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
            onClick = {navController.navigate(Routes.NavigationSelectionScreen.route) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Start Navigating")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = {navController.navigate(Routes.LogsScreen.route)},
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Previous logs")
        }
    }
}

