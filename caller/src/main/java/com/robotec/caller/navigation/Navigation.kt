package com.robotec.caller.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.robotec.caller.listener.Status
import com.robotec.caller.speak.Voice
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener
import com.robotemi.sdk.navigation.listener.OnDistanceToDestinationChangedListener
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener

import com.robotemi.sdk.navigation.model.Position

class Navigation {

    private var temiRobot: Robot = Robot.getInstance()
    private var speak = Voice()

    fun goToDistance(useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                val distanceStatus = object : OnDistanceToDestinationChangedListener {
                    override fun onDistanceToDestinationChanged(
                        location: String,
                        distance: Float,
                    ) {
                        Status.currentDistanceStatus = distance
                        temiRobot.removeOnDistanceToDestinationChangedListener(this)

                    }
                }
                temiRobot.addOnDistanceToDestinationChangedListener(distanceStatus)

            } else {
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao calcular distancia")
            onComplete.invoke()
        }
    }

    fun goTo(local: String, useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                val location = local.lowercase().trim { it <= ' ' }

                if (!temiRobot.locations.contains(location)) {
                    Toast.makeText(context, "Local não existe", Toast.LENGTH_SHORT).show()
                    onComplete.invoke()
                }

                temiRobot.goTo(location)

                val goToStatus = object : OnGoToLocationStatusChangedListener {
                    override fun onGoToLocationStatusChanged(
                        location: String,
                        status: String,
                        descriptionId: Int,
                        description: String,
                    ) {

                        if (description == "Obstáculo de Altura" || description == "Obstáculo Lidar") {
                            speak.startSpeak("Com licença, por favor", true, context) {}
                        }
                        Status.currentNavigationStatus = status
                        if (description.isNotBlank()) {
                            if (status == "complete") {
                                temiRobot.removeOnGoToLocationStatusChangedListener(this)
                                onComplete.invoke()

                            }
                            if (status == "abort") {
                                temiRobot.removeOnGoToLocationStatusChangedListener(this)
                                onComplete.invoke()
                            }
                        }
                    }
                }
                temiRobot.addOnGoToLocationStatusChangedListener(goToStatus)
            } else {
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar, local: $local")
            onComplete.invoke()
        }
    }

    fun goToPosition(x: Float, y: Float, angle: Float, onComplete: () -> Unit) {
        try {

            temiRobot.goToPosition(Position(x, y, angle, 0), backwards = false, noBypass = false)

            val goToStatus = object : OnCurrentPositionChangedListener {
                override fun onCurrentPositionChanged(
                    position: Position,
                ) {
                    Log.w("Navigation", "Posição do robô: $position")
                    temiRobot.removeOnCurrentPositionChangedListener(this)
                    onComplete.invoke()
                }
            }
            temiRobot.addOnCurrentPositionChangedListener(goToStatus)

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar local")
            onComplete.invoke()
        }
    }

    fun saveLocal(local: String, onComplete: () -> Unit) {
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
            onComplete.invoke()
        }
    }

    fun returnBase(useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                val locals = temiRobot.locations

                temiRobot.goTo(locals[0])

                val goToStatus = object : OnGoToLocationStatusChangedListener {
                    override fun onGoToLocationStatusChanged(
                        location: String,
                        status: String,
                        descriptionId: Int,
                        description: String,
                    ) {
                        if (description == "Obstáculo de Altura" || description == "Obstáculo Lidar") {
                            speak.startSpeak("Com licença, por favor", true, context) {}
                        }
                        if (description.isNotBlank()) {
                            if (status == "complete") {
                                temiRobot.removeOnGoToLocationStatusChangedListener(this)
                                onComplete.invoke()
                            }
                            if (status == "abort") {
                                temiRobot.removeOnGoToLocationStatusChangedListener(this)
                                onComplete.invoke()
                            }
                        }
                    }
                }
                temiRobot.addOnGoToLocationStatusChangedListener(goToStatus)
            } else {
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar")
            onComplete.invoke()
        }
    }

    fun stop(onComplete: () -> Unit) {
        try {
            temiRobot.stopMovement()
            onComplete.invoke()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao parar")
            onComplete.invoke()
        }
    }

    fun deleteLocal(location: String, onComplete: () -> Unit) {
        try {
            temiRobot.deleteLocation(location)
            Log.e("TemiCaller", "Local $location deletado")
            onComplete.invoke()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao deletar local")
            onComplete.invoke()
        }
    }

    fun getLocations(): List<String> {
        return temiRobot.locations
    }

    fun goToComplex(local: String, backwards: Boolean, noBypass: Boolean, onComplete: () -> Unit) {
        
        try {
            val location = local.lowercase().trim { it <= ' ' }

            if (!temiRobot.locations.contains(location)) {
                Log.w("Navigation", "Local não existe")
            }
            temiRobot.goTo(location, backwards, noBypass)

            val distanceStatus = object : OnDistanceToLocationChangedListener {
                override fun onDistanceToLocationChanged(
                    distances: Map<String, Float>
                ) {
                    var text = "Distance:\n"
                    for (location in distances.keys) {
                        text +=
                            " -> $location :: ${distances[location]}\n"
                    }
                    Log.w("Navigation", text)
                    temiRobot.removeOnDistanceToLocationChangedListener(this)
                }
            }
            temiRobot.addOnDistanceToLocationChangedListener(distanceStatus)

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
                            com.robotec.caller.listener.Status.currentNavigationStatus = status
                            Log.d("TemiCaller", "Status GOTO: $status")
                            temiRobot.removeOnGoToLocationStatusChangedListener(this)
                            onComplete.invoke()

                        }
                        if (status == "abort") {
                            com.robotec.caller.listener.Status.currentNavigationStatus = status
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
            onComplete.invoke()
        }
    }

    fun reposicionar(onComplete: () -> Unit) {
        try {
            temiRobot.repose()

            val reposeStatus = object : OnReposeStatusChangedListener {
                override fun onReposeStatusChanged(
                    status: Int,
                    description: String,
                ) {
                    Log.d("Descrição de reposição", description)

                    if (description.isNotBlank()) {
                        if (status == 4) {
                            Log.d("TemiCaller", "Status repose: $status")
                            temiRobot.removeOnReposeStatusChangedListener(this)
                            onComplete.invoke()
                        }
                    }
                }
            }
            temiRobot.addOnReposeStatusChangedListener(reposeStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao se posicionar")
        }
    }
}