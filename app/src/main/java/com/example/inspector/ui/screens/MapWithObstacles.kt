package com.example.inspector.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.inspector.MapVisualizer
import com.example.inspector.ObstacleManager

@Preview
@Composable
fun MapWithObstacles() {
    val map = MapVisualizer.mapBitmap
    val obstacles = ObstacleManager.obstacles

    if (map != null) {
        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {

            Image(
                bitmap = map.asImageBitmap(),
                contentDescription = "Robot Map",
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                obstacles.forEach { obs ->
                    val (px, py) = MapVisualizer.robotToPixel(obs.x, obs.y)
                    drawCircle(
                        color = Color.Red,
                        radius = 6f,
                        center = androidx.compose.ui.geometry.Offset(px, py)
                    )
                }
            }
        }
    }
}
