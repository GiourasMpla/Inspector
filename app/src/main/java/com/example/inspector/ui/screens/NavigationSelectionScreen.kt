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
import com.example.inspector.normalNavigate

@Composable
fun NavigationSelectionScreen(
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
            onClick = { navigatingToStartPoint(navController) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Go to Start Point")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = { navigatingToEndPoint(navController) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Go to end point")
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            onClick = { navigatingToChargingStation(navController) },
            modifier = Modifier.height(68.dp)
        ){
            Text(text = "Go to charging station")
        }
    }
}


// θέλει update όταν προσθέσουμε τα σημεία του χάρτη και αυτό γιατί να μπορεί να κρίνει το ρομπότ προς τα που θα πάει - NOT Completed
fun navigatingToStartPoint(navController: NavHostController){
    normalNavigate("Start Point")
    navController.navigate(Routes.NavigationStatusScreen.route)
}

fun navigatingToEndPoint(navController: NavHostController){
    normalNavigate("End Point")
    navController.navigate(Routes.NavigationStatusScreen.route)
}

fun navigatingToChargingStation(navController: NavHostController){
    normalNavigate("Charging Station")
    navController.navigate(Routes.NavigationStatusScreen.route)
}