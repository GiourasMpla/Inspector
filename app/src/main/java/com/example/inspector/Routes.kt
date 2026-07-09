package com.example.inspector

sealed class Routes(val route: String) {

    data object StartingScreen : Routes("StartingScreen")
    data object NavigationSelectionScreen : Routes("NavigationSelectionScreen")
    data object NavigationStatusScreen : Routes("NavigationStatusScreen")
    data object LogsScreen : Routes("LogsScreen")
}