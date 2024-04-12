package com.robotec.caller.speak

import android.util.Log
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.SttLanguage
import com.robotec.caller.utils.Request
import com.robotec.caller.listener.Status

class Voice {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun startSpeak(text: String, useTemi: Boolean, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                temiRobot.speak(TtsRequest.create(text, false))

                val speakStatus = object : Robot.TtsListener {
                    override fun onTtsStatusChanged(
                        TtsRequest: TtsRequest,
                    ) {
                        Status.currentSpeakStatus = TtsRequest.status.toString()

                        if (TtsRequest.status.toString() == "STARTED") {
                            temiRobot.removeTtsListener(this)
                            onComplete.invoke()
                        }
                        if (TtsRequest.status.toString() == "ABORT") {
                            Log.e("TemiCaller", "Erro ao usar speak")
                            temiRobot.removeTtsListener(this)
                            onComplete.invoke()
                        }
                    }
                }
                temiRobot.addTtsListener(speakStatus)

            } else {
                Log.v("TemiCaller", "Sem usar o speak")
                onComplete.invoke()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao usar speak")
        }
    }

    fun finishSpeak(text: String, useTemi: Boolean, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                temiRobot.speak(TtsRequest.create(text, false))

                val speakStatus = object : Robot.TtsListener {
                    override fun onTtsStatusChanged(
                        TtsRequest: TtsRequest,
                    ) {
                        Status.currentSpeakStatus = TtsRequest.status.toString()

                        if (TtsRequest.status.toString() == "COMPLETED") {
                            temiRobot.removeTtsListener(this)
                            onComplete.invoke()
                        }
                        if (TtsRequest.status.toString() == "ABORT") {
                            Log.e("TemiCaller", "Erro ao usar speak")
                            temiRobot.removeTtsListener(this)
                            onComplete.invoke()
                        }
                    }
                }
                temiRobot.addTtsListener(speakStatus)

            } else {
                Log.v("TemiCaller", "Sem usar o speak")
                onComplete.invoke()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao usar speak")
        }
    }

    fun wakeUp(onComplete: () -> Unit) {

        mutarAlexa()

        request.requestToBeKioskApp()

        enableWakeup()
        temiRobot.wakeup()

        try {
            val asrStatus = object : Robot.AsrListener {
                override fun onAsrResult(
                    asrResult: String,
                    sttLanguage: SttLanguage,
                ) {

                    when {
                        asrResult == null -> {
                            temiRobot.finishConversation()
                            temiRobot.removeAsrListener(this)
                        }
                        asrResult.isEmpty() -> {
                            temiRobot.finishConversation()
                            temiRobot.removeAsrListener(this)
                        }
                        else -> {
                            Status.currentAsrStatus = asrResult
                            temiRobot.finishConversation()
                            temiRobot.removeAsrListener(this)
                        }
                    }
                }
            }
            temiRobot.addAsrListener(asrStatus)
        }  catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }
    }

    fun stopSpeak() {
        temiRobot.cancelAllTtsRequests()
    }

    private fun enableWakeup() {
        request.requestSettings()
        temiRobot.toggleWakeup(false)
    }

    private fun mutarAlexa() {
        temiRobot.muteAlexa()
    }
}