package com.robotec.caller.speak

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.SttLanguage
import com.robotec.caller.utils.Request
import com.robotec.caller.listener.Status

class Voice {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun startSpeak(text: String, useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {

                val tts = TtsRequest.create(text, isShowOnConversationLayer = false, cached = true)
                temiRobot.speak(tts)

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
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
                onComplete.invoke()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao usar speak")
        }
    }

    fun finishSpeak(text: String, useTemi: Boolean, context: Context, onComplete: () -> Unit) {
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
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
                onComplete.invoke()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao usar speak")
        }
    }

    fun finishWithoutSpeak(useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {

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
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
                onComplete.invoke()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao usar speak")
        }
    }

    fun wakeUp(useTemi: Boolean, context: Context, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                mutarAlexa()

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
                                Status.currentAsrStatus = asrResult
                                temiRobot.finishConversation()
                                temiRobot.removeAsrListener(this)
                            }
                        }
                    }
                }
                temiRobot.addAsrListener(asrStatus)
            } else {
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao reconhecer")
        }
    }

    fun stopSpeak(useTemi: Boolean, context: Context) {
        try {
            if (useTemi) {
                temiRobot.cancelAllTtsRequests()
            } else {
                Toast.makeText(context, "Sem uso do Temi", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao cancelar a voz")
        }

    }

    fun subtitle(text: String, useTemi: Boolean): Map<String, Float> {
        try {
            if (useTemi) {
                val tamanhoMaximoLegenda = 79
                val legendasComTempos = mutableMapOf<String, Float>()

                val palavras = text.split(" ")

                var legendaAtual = ""

                for (palavra in palavras) {
                    if (legendaAtual.length + palavra.length + 1 > tamanhoMaximoLegenda) {
                        val tempo = time(legendaAtual.trim())
                        legendasComTempos[legendaAtual.trim()] = tempo
                        legendaAtual = ""
                    }

                    legendaAtual += if (legendaAtual.isEmpty()) palavra else " $palavra"
                }

                if (legendaAtual.isNotEmpty()) {
                    val tempofinal = time(legendaAtual.trim())
                    legendasComTempos[legendaAtual.trim()] = tempofinal
                }

                return legendasComTempos
            } else {
                return emptyMap()
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao cancelar a voz")
            return emptyMap()
        }
    }

    fun replaceWord(text: String, find: String, replace: String): String {
        return text.replace(find, replace, ignoreCase = true)
    }

    private fun time(text: String): Float {
        val textoWithoutSpaces = text.replace(" ", "")
        val punctuationList = mutableListOf<Char>()

        var charCount = 0
        for (char in textoWithoutSpaces) {
            if (char == '?' || char == ',' || char == '!' || char == '.') {
                punctuationList.add(char)
            } else {
                charCount++
            }
        }

        val carac = punctuationList.size * 12
        val seg = charCount / 2
        val div = seg * 9

        val tokenValue = (div+carac) / 60.0

        return tokenValue.toFloat() * 1000
    }

    private fun enableWakeup() {
        request.requestSettings()
        temiRobot.toggleWakeup(false)
    }

    private fun mutarAlexa() {
        temiRobot.muteAlexa()
    }

}
