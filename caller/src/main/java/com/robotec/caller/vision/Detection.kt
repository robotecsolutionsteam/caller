package com.robotec.caller.vision

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnUserInteractionChangedListener
import com.robotemi.sdk.model.DetectionData

class Detection {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    init {
        request.requestToBeKioskApp()
    }

    fun detectionOn(distance: Float, onComplete: () -> Unit) {
        request.requestDetectionMode()

        try {
            temiRobot.setDetectionModeOn(true, distance)

            val detectionStatus = object : OnDetectionStateChangedListener {
                override fun onDetectionStateChanged(
                    state: Int
                ) {
                    if (state == 2) {
                        temiRobot.removeOnDetectionStateChangedListener(this)
                        onComplete.invoke()

                    }
                }
            }
            temiRobot.addOnDetectionStateChangedListener(detectionStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }
    }

    fun trackerOn() {
        try {
            request.requestTrackUser()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }

    }

    fun interaction(onComplete: () -> Unit) {
        try {

            val interactionStatus = object : OnUserInteractionChangedListener {
                override fun onUserInteraction(
                    isInteracting: Boolean
                ) {
                    if (isInteracting) {
                        temiRobot.removeOnUserInteractionChangedListener(this)
                        onComplete.invoke()

                    }
                }
            }
            temiRobot.addOnUserInteractionChangedListener(interactionStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer interacao")
        }
    }

    fun dataDetection(onComplete: () -> Unit) {
        try {

            val dataStatus = object : OnDetectionDataChangedListener {
                override fun onDetectionDataChanged(
                    detectionData: DetectionData
                ) {
                    // TODO: fazer retornar a resposta
                    temiRobot.removeOnDetectionDataChangedListener(this)
                    onComplete.invoke()
                }
            }
            temiRobot.addOnDetectionDataChangedListener(dataStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }
    }

}
