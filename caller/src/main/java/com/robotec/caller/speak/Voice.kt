package com.robotec.caller.speak

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.robotemi.sdk.SttLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Voice {

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
                        com.robotec.caller.listener.Status.currentSpeakStatus = TtsRequest.status.toString()
                        temiRobot.removeTtsListener(this)
                        onComplete.invoke()
                    }
                    if (TtsRequest.status.toString() == "ABORT") {
                        com.robotec.caller.listener.Status.currentSpeakStatus = TtsRequest.status.toString()
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

    fun wakeUp(onComplete: () -> Unit) {

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
                            println("Null: $asrResult")
                            temiRobot.removeAsrListener(this)
                        }
                        asrResult.isEmpty() -> {
                            temiRobot.finishConversation()
                            println("Empty: $asrResult")
                            temiRobot.removeAsrListener(this)
                        }
                        else -> {
                            Log.d("ASR", asrResult)
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

    fun enableWakeup() {
        request.requestSettings()
        temiRobot.toggleWakeup(false)
    }
}