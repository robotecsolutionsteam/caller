package com.robotec.temi.navigation

import android.util.Log
import com.robotec.temi.listener.Status
import com.robotec.temi.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.navigation.model.SpeedLevel

class Navigation {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun goTo(local: String, onComplete: () -> Unit) {
        try {
            val location = local.lowercase().trim { it <= ' ' }

            if (!temiRobot.locations.contains(location)) {
                Log.w("Navigation", "Local não existe")
            }

            temiRobot.goTo(location)

            val goToStatus = object : OnGoToLocationStatusChangedListener {
                override fun onGoToLocationStatusChanged(
                    location: String,
                    status: String,
                    descriptionId: Int,
                    description: String,
                ) {
                    Log.w("Navigation", "Descrição: $description")
                    if (description == "Obstáculo de Altura" || description == "Obstáculo Lidar") {
                        temiRobot.speak(TtsRequest.create("Com licença, por favor", false))
                    }

                    if (description.isNotBlank()) {
                        if (status == "complete") {
                            Status.currentNavigationStatus = status
                            Log.d("TemiCaller", "Status GOTO: $status")
                            temiRobot.removeOnGoToLocationStatusChangedListener(this)
                            onComplete.invoke()

                        }
                        if (status == "abort") {
                            Status.currentNavigationStatus = status
                            temiRobot.removeOnGoToLocationStatusChangedListener(this)
                            Log.e("Navigation", "Status GOTO: $status")
                            onComplete.invoke()
                        }
                    }
                }
            }
            temiRobot.addOnGoToLocationStatusChangedListener(goToStatus)

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar, local: $local")
        }
    }

    fun saveLocal(local: String) {
        try {
            val location = local.lowercase().trim { it <= ' ' }
            val result = temiRobot.saveLocation(location)
            if (result) {
                Log.d("Navigation", "Local $location salvo com sucesso")
            } else {
                Log.e("Navigation", "Não foi possivel salvar o local $location")
            }
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao salvar o local")
        }
    }

    fun returnBase(onComplete: () -> Unit) {
        try {
            val locals = temiRobot.locations

            temiRobot.goTo(locals[0])

            val goToStatus = object : OnGoToLocationStatusChangedListener {
                override fun onGoToLocationStatusChanged(
                    location: String,
                    status: String,
                    descriptionId: Int,
                    description: String,
                ) {
                    Log.d("Navigation description", description)
                    if (description == "Obstáculo de Altura" || description == "Obstáculo Lidar") {
                        temiRobot.speak(TtsRequest.create("Com licença, por favor", false))
                    }

                    Log.w("navegacao", description)
                    if (description.isNotBlank()) {
                        if (status == "complete") {
                            Log.d("TemiCaller", "Status GOTO: $status")
                            temiRobot.removeOnGoToLocationStatusChangedListener(this)
                            onComplete.invoke()
                        }
                        if (status == "abort") {
                            temiRobot.removeOnGoToLocationStatusChangedListener(this)
                            Log.e("Navigation", "Status GOTO: $status")
                            onComplete.invoke()
                        }
                    }
                }
            }
            temiRobot.addOnGoToLocationStatusChangedListener(goToStatus)

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar")
        }
    }
}