package com.robotec.temi.speak

import android.util.Log
import com.robotec.temi.listener.Status
import com.robotec.temi.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest

import android.content.pm.PackageManager
import com.robotemi.sdk.SttLanguage
import com.robotemi.sdk.constants.SdkConstants

class Voice {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    enum class sttLanguage(val value: Int) {
        SYSTEM(0),
        EN_US(1),
        ZH_CN(2),
        JA_JP(3),
        KO_KR(4),
        ZH_HK(5),
        ZH_TW(6),
        DE_DE(7),
        TH_TH(8),
        IN_ID(9),
        PT_BR(10),
        AR_EG(11),
        FR_CA(12),
        FR_FR(13),
        ES_ES(14),
        CA_ES(15),
        IW_IL(16),
        IT_IT(17),
        ET_EE(18),
        TR_TR(19);
    }

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

    fun wakeUp(onComplete: () -> Unit) {

        request.requestToBeKioskApp()

        temiRobot.wakeup()

        enableWakeup()

//            val wakeStatus = object : Robot.WakeupWordListener {
//                override fun onWakeupWord(
//                    wakeupWord: String,
//                    direction: Int
//                ) {
//                    println("WAKEUP: $wakeupWord , $direction")
//                    if (wakeupWord == "hi temi") {
//
//                        temiRobot.removeWakeupWordListener(this)
//                    }
//                }
//            }
//            temiRobot.addWakeupWordListener(wakeStatus)

        val asrStatus = object : Robot.AsrListener {
            override fun onAsrResult(asrResult: String, sttLanguage: SttLanguage) {
                println("ASR: $asrResult")
                temiRobot.addAsrListener(this)
            }
        }
        temiRobot.removeAsrListener(asrStatus)

        disableWakeup()

    }

//    private fun disableWakeup() {
//        request.requestSettings()
//
//        Log.d("disableWakeup(?):", Robot.getInstance().isReady.toString());
//        Robot.getInstance().wakeup();
//    }

    //toggleWakeup(false); wakeup(); toggleWakeup(true)

    private fun enableWakeup() {
        temiRobot.toggleWakeup(false)
    }

    private fun disableWakeup() {
        temiRobot.toggleWakeup(true)
    }

    private fun wakeup() {
        temiRobot.wakeup()
    }
}