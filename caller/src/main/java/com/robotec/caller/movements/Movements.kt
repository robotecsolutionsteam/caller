package com.robotec.caller.movements

import android.util.Log
import com.robotec.caller.listener.Status
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener
import com.robotemi.sdk.listeners.OnMovementVelocityChangedListener
import com.robotemi.sdk.listeners.OnRobotLiftedListener

class Movements {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun moveRobot(distance_x: Float, distance_y: Float, onComplete: () -> Unit) {
        try {
            if (distance_x > -1 && distance_x < 1) {
                Log.w("Movements", "Distancia em x inválida")
            }
            if (distance_y > -1 && distance_y < 1) {
                Log.w("Movements", "Distancia em y inválida")
            }
            temiRobot.skidJoy(distance_x, distance_y)

            val moveStatus = object : OnMovementStatusChangedListener {
                override fun onMovementStatusChanged(
                    type: String,
                    status: String
                ) {
                    if (status == "complete") {
                        com.robotec.caller.listener.Status.currentMovementStatus = status
                        Log.d("TemiCaller", "Status Movement: $status")
                        temiRobot.removeOnMovementStatusChangedListener(this)
                        onComplete.invoke()
                    }
                    if (status == "abort") {
                        com.robotec.caller.listener.Status.currentMovementStatus = status
                        temiRobot.removeOnMovementStatusChangedListener(this)
                        Log.e("Movements", "Status Movement: $status")
                        onComplete.invoke()
                    }
                }
            }
            temiRobot.addOnMovementStatusChangedListener(moveStatus)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao movimentar o robô")
            onComplete.invoke()
        }
    }

    fun turnRobot(angulo: Int, velocidade: Float, onComplete: () -> Unit) {
        try {
            if (velocidade > -1 && velocidade < 1) {
                Log.w("Movements", "Velocidade inválida")
            }
            temiRobot.turnBy(angulo, velocidade)
            val moveStatus = object : OnMovementStatusChangedListener {
                override fun onMovementStatusChanged(
                    type: String,
                    status: String
                ) {
                    if (status == "complete") {
                        com.robotec.caller.listener.Status.currentMovementStatus = status
                        Log.d("TemiCaller", "Status Movement: $status")
                        temiRobot.removeOnMovementStatusChangedListener(this)
                        onComplete.invoke()

                    }
                    if (status == "abort") {
                        com.robotec.caller.listener.Status.currentMovementStatus = status
                        temiRobot.removeOnMovementStatusChangedListener(this)
                        Log.e("Movements", "Status Movement: $status")
                        onComplete.invoke()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao virar o robô")
            onComplete.invoke()
        }
    }

    fun tiltHead(angulo: Int, velocidade: Float, onComplete: () -> Unit) {
        try {
            if (angulo < -25 || angulo > 55) {
                Log.w("Movements", "Angulo inválido")
            }
            if (velocidade > -1 && velocidade < 1) {
                Log.w("Movements", "Velocidade inválida")
            }
            temiRobot.tiltAngle(angulo, velocidade)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao inclinar a cabeça")
            onComplete.invoke()
        }
    }

    fun tiltHeadBy(angulo: Int, velocidade: Float, onComplete: () -> Unit) {
        try {
            temiRobot.tiltBy(angulo, velocidade)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao inclinar a cabeça")
            onComplete.invoke()
        }
    }

    fun stopMovement() {
        try {
            temiRobot.stopMovement()
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao parar o robô")
        }
    }

    fun placesPatrol(
        lugares: List<String>,
        semParar: Boolean,
        nVezes: Int,
        tempoEspera: Int,
        onComplete: () -> Unit
    ) {
        try {
            temiRobot.patrol(lugares, semParar, nVezes, tempoEspera)
        } catch (e: Exception) {
            Log.e("TemiCaller", "Erro ao iniciar patrulha")
            onComplete.invoke()
        }
    }

    fun getSpeed(onComplete: () -> Unit): Float {
        val robotSpeed = object : OnMovementVelocityChangedListener {
            override fun onMovementVelocityChanged(velocity: Float) {
                Log.w("Movements", "Velocidade: $velocity")
                com.robotec.caller.listener.Status.currentRobotSpeed = velocity
                onComplete.invoke()
            }
        }
        temiRobot.addOnMovementVelocityChangedListener(robotSpeed)
        return Status.currentRobotSpeed
    }

    fun getLiftedState(onComplete: () -> Unit):   Boolean {
        val liftedStatus = object : OnRobotLiftedListener {
            override fun onRobotLifted(isLifted: Boolean, reason: String) {
                Log.w("Movements", "Levantado: $isLifted")
                com.robotec.caller.listener.Status.currentLiftedStatus = isLifted
                onComplete.invoke()
            }

        }
        temiRobot.addOnRobotLiftedListener(liftedStatus)
        return Status.currentLiftedStatus
    }
}