package com.robotec.caller.utils

import android.util.Log
import android.widget.Toast
import com.robotemi.sdk.Robot
import android.content.Context
import com.robotemi.sdk.constants.HardButton
import com.robotemi.sdk.permission.Permission

private const val REQUEST_CODE_NORMAL = 0

class Config {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    private var estaTravado: Boolean = false

    init {
        desabilidarNav()
        desabilidarTop()
        desabilidarWakeup()
        desabilidarReturn()
        alexa()
    }

    fun block(context: Context, useTemi: Boolean){
        try {
            if (useTemi) {
                if (!estaTravado) {
                    travar(context)
                    estaTravado = true
                } else {
                    destravar(context)
                    estaTravado = false
                }
            } else {
                Log.v("TemiCaller", "Sem usar o temi ao bloqueiar")
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao bloqueiar")
        }
    }

    private fun travar(context: Context) {
        try {
            temiRobot.hideTopBar()
            temiRobot.isHardButtonsDisabled = true
            temiRobot.setHardButtonMode(HardButton.POWER, HardButton.Mode.DISABLED)
            temiRobot.setHardButtonMode(HardButton.VOLUME, HardButton.Mode.DISABLED)

            desabilidarTop()
            Toast.makeText(context, "Botões bloquados", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao bloqueiar: ${e.message}")
        }
    }

    private fun destravar(context: Context) {
        try {
            temiRobot.showTopBar()
            temiRobot.isHardButtonsDisabled = false
            habilidarTop()
            Toast.makeText(context, "Botões desbloquados", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao desbloqueiar: ${e.message}")
        }
    }

    private fun habilidarTop() {
        try {
            if (request.requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
                return
            }
            temiRobot.topBadgeEnabled = true
        } catch (e: Exception) {
            Log.e("temicaller", "Erro ao habilitar topBagde")
        }
    }

    private fun desabilidarNav() {
        try {
            if (request.requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
                return
            }
            temiRobot.toggleNavigationBillboard(true)
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao desativar toggleNavigationBillboard: ${e.message}")
        }
    }

    private fun desabilidarTop() {
        try {
            if (request.requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
                return
            }
            temiRobot.topBadgeEnabled = false
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao desativar topBagde: ${e.message}")
        }
    }

    private fun desabilidarWakeup() {
        try {
            if (request.requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
                return
            }
            temiRobot.toggleWakeup(true)
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao desabilitar wakeup: ${e.message}")
        }
    }

    private fun desabilidarReturn() {
        try {
            if (request.requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
                return
            }
            temiRobot.autoReturnOn = false
        } catch (e: Exception) {
            Log.e("Robot", "Erro ao desabilitar return: ${e.message}")
        }
    }

    private fun alexa() {
        temiRobot.muteAlexa()
    }
}