package com.robotec.temi

import android.util.Log
import com.robotec.temi.listener.Status
import com.robotec.temi.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest

import android.content.pm.PackageManager
import com.robotemi.sdk.constants.SdkConstants


class Speak {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun speak(text: String, onComplete: () -> Unit) {
        try {
            temiRobot.speak(TtsRequest.create(text, false))

            val speakStatus = object : Robot.TtsListener {
                override fun onTtsStatusChanged(
                    TtsRequest: TtsRequest,
                ) {
                    if (TtsRequest.status.toString() == "COMPLETED") {
                        Status.currentSpeakStatus = TtsRequest.status.toString()
                        temiRobot.removeTtsListener(this)
                        onComplete.invoke()
                    }
                    if (TtsRequest.status.toString() == "ABORT") {
                        Status.currentSpeakStatus = TtsRequest.status.toString()
                        Log.e("Speak", "Falha ao falar")
                        temiRobot.removeTtsListener(this)
                        onComplete.invoke()
                    }
                }
            }
            temiRobot.addTtsListener(speakStatus)

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar")
        }
    }

    fun wakeUp(text: String, onComplete: () -> Unit) {

        request.requestToBeKioskApp()

        try {
            val wakeStatus = object : Robot.WakeupWordListener {
                override fun onWakeupWord(
                    wakeupWord: String,
                    direction: Int
                ) {
                    if (wakeupWord == "hi temi") {
                        val asrStatus = object : Robot.AsrListener {
                            override fun onAsrResult(
                                asrResult: String,
                            ) { 
                                println("ASR: $asrResult")
                                temiRobot.addAsrListener(this)
                            }
                        }
                        temiRobot.removeAsrListener(asrStatus)
                        temiRobot.removeWakeupWordListener(this)
                    }
                }
            }
            temiRobot.addWakeupWordListener(wakeStatus)

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao navegar")
        }
    }
}