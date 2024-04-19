package com.robotec.caller.navigation

import android.util.Log
import com.robotec.caller.listener.Status
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.Floor
import com.robotemi.sdk.map.OnLoadFloorStatusChangedListener
import com.robotemi.sdk.navigation.model.Position

class MultiFloor {
    private var temiRobot: Robot = Robot.getInstance()

    private val request = Request()

    init {
        request.requestMaps()
    }

    fun getCurrentFloor(): Floor? {
        try {
            return temiRobot.getCurrentFloor()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao obter o estado atual multifloors")
        }
        return null
    }

    fun getFloors(): List<Floor>? {
        try {
            return temiRobot.getAllFloors()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao obter o multifloors")
        }
        return null
    }

    fun loadFloor(id: Int, position: Position, onComplete: () -> Unit) {
        try {
            temiRobot.loadFloor(id, position)
            val loadFloorStatus = object : OnLoadFloorStatusChangedListener {
                override fun onLoadFloorStatusChanged(status: Int) {
                    Status.currentLoadFloorStatus = status
                    if (status == 0) {
                        onComplete.invoke()
                        temiRobot.removeOnLoadFloorStatusChangedListener(this)
                    }
                    if (status == -1) {
                        onComplete.invoke()
                        temiRobot.removeOnLoadFloorStatusChangedListener(this)
                    }
                }
            }
            temiRobot.addOnLoadFloorStatusChangedListener(loadFloorStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro mudar de mapa")
            onComplete.invoke()
        }
    }
}