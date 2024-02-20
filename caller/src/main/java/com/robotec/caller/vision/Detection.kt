package com.robotec.caller.vision

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener

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

    fun trackerOn(distance: Float) {
        request.requestTrackUser()

        try {
            temiRobot.trackUserOn
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }

    }

    fun interaction() {
        
    }

}
