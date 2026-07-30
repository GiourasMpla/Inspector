package com.example.inspector.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inspector.ObstacleManager
import com.example.inspector.Routes
import java.util.Date


@Composable
fun LogsScreen(
    robotConnected: Boolean = true,
    sdkStatus: String = "Preview",
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val obstacles = ObstacleManager.obstacles
        LazyColumn(
            modifier = Modifier.padding(16.dp).weight(1f)
        ) {
            items(obstacles) { obs ->
                Text("Obstacle near ${ObstacleManager.nearestLocation(obs.x, obs.y)}")
                Text("Obstacle at x=${obs.x}, y=${obs.y}, θ=${obs.theta}")
                //Text("Time: ${Date(obs.timestamp)}")
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = { navController.navigate(Routes.StartingScreen.route) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Return to starting screen")
        }
    }
}