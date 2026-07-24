package com.example.inspector

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.ainirobot.coreservice.client.ashmem.ShareMemoryApi
//import com.google.android.gms.common.util.MapUtils
import com.ainirobot.coreservice.client.map.MapUtils
import java.io.FileDescriptor
import java.io.FileInputStream

object MapVisualizer {

    var mapBitmap by mutableStateOf<Bitmap?>(null)
    var resolution = 0.05f      // default, will be replaced by RoverMap
    var originX = 0f
    var originY = 0f

    fun loadMap() {
        try {
            val mapName = CurrentMap.name
            if (mapName.isEmpty()) {
                Log.e("MapVisualizer", "Map name is empty, cannot load map")
                return
            }

            val pfd = ShareMemoryApi.getInstance().getMapPgmPFD(mapName)
            val fd: FileDescriptor = pfd.fileDescriptor
            val inputStream = FileInputStream(fd)

            // Convert PGM → RoverMap → Bitmap
            val roverMap = MapUtils.loadPFD2RoverMap(inputStream)

            mapBitmap = roverMap.bitmap
            resolution = roverMap.resolution
            originX = roverMap.originX
            originY = roverMap.originY

            ShareMemoryApi.getInstance().releaseGetMapPgmPFD()

            Log.d("MapVisualizer", "Map loaded successfully")

        } catch (e: Exception) {
            Log.e("MapVisualizer", "Error loading map: ${e.message}")
        }
    }

    fun robotToPixel(x: Double, y: Double): Pair<Float, Float> {
        val px = ((x - originX) / resolution)
        val py = ((y - originY) / resolution)
        return Pair(px.toFloat(), py.toFloat())
    }
}