package com.robotec.caller.navigation

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapModel
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener

class Maps {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    init {
        request.requestMaps()
    }

    fun mapImage(id: String): Any? {

        try {
            val maps = temiRobot.getMapList()

            if (!maps.isEmpty()) {
                for (map in maps) {

                    if (map.id == id) {
                        val mapDataModel = temiRobot.getMapData()
                        return mapDataModel!!.mapImage
                    }
                }
            }

            return null

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar mostrar imagem do mapa")
            return null
        }
    }

    fun mapList(onComplete: () -> Unit): List<MapModel> {

        try {
            val maps = temiRobot.getMapList()

            return maps
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar mostrar lista de mapas")
            return emptyList()
        }
    }

    fun loadMap(id: String, onComplete: () -> Unit) {
        try {
            temiRobot.loadMap(id)

            val mapsStatus = object : OnLoadMapStatusChangedListener {
                override fun onLoadMapStatusChanged(
                    status: Int,
                    requestId: String,
                ) {

                    if (status == 0) {
                        temiRobot.removeOnLoadMapStatusChangedListener(this)
                        onComplete.invoke()

                    }
                }
            }
            temiRobot.addOnLoadMapStatusChangedListener(mapsStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao abrir mapa")
            onComplete.invoke()
        }
    }
}