package com.robotec.caller.speak

import android.util.Log
import com.robotec.caller.listener.Status
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest

import android.content.pm.PackageManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.robotemi.sdk.SttLanguage
import com.robotemi.sdk.constants.SdkConstants
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

    fun wakeUp() {

        request.requestToBeKioskApp()

        enableWakeup()
        temiRobot.wakeup()

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
                        temiRobot.finishConversation()
                        createRetrofit(asrResult + ", me responda em uma frase", "xxxx")
                        temiRobot.removeAsrListener(this)
                    }
                }
            }
        }
        temiRobot.addAsrListener(asrStatus)

    }

    fun enableWakeup() {
        request.requestSettings()
        temiRobot.toggleWakeup(false)
    }

    fun disableWakeup() {
        request.requestSettings()
        temiRobot.toggleWakeup(true)
    }

    fun wakeup() {
        temiRobot.wakeup()
    }

    private fun createRetrofit(userInput: String, apiKey: String) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(500, TimeUnit.SECONDS)
            .writeTimeout(500, TimeUnit.SECONDS)
            .build()

        val gson: Gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.temi.jabuti.ai:8000/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiService = retrofit.create(JabutiApiService::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = apiService.ask(apiKey, userInput)

            if (response.isSuccessful) {
                val responseBody = response.body()
                println("Corpo da Resposta: $responseBody")

            } else {
                Log.e("TemiCaller", "Erro na requisição: ${response.code()}")
                Log.e("TemiCaller", "Corpo da resposta: ${response.errorBody()?.string()}")
            }
            temiRobot.wakeupWordDisabled
        }
    }
}