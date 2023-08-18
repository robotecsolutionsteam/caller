package com.robotec.temi

import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest

/**
 * Esta classe fornece métodos para interagir com o robô TEMI, como controlar o movimento,
 * ajustar ângulos, executar comandos de voz e interagir com o usuário.
 *
 * @property temiRobot Instância do robô TEMI.
 */

class Commands {

    /**
     * Inicializa a classe, obtendo uma instância do robô TEMI.
     */
    private var temiRobot: Robot

    init {
        temiRobot = Robot.getInstance()
    }

    /**
     * Faz com que o robô vá para a posição especificada.
     *
     * @param selectPosition A posição para a qual o robô deve ir.
     */
    fun goto(selectPosition: String) {
        temiRobot.goTo(selectPosition)
    }

    /**
     * Faz com que o robô gire pelo valor especificado.
     *
     * @param value O valor do ângulo pelo qual o robô deve girar.
     */
    fun turnBy(value: Int) {
        temiRobot.turnBy(value)
    }

    /**
     * Ajusta o ângulo de inclinação do robô.
     *
     * @param value O valor do ângulo de inclinação desejado.
     */
    fun angle(value: Int) {
        temiRobot.tiltAngle(value)
    }

    /**
     * Ajusta o ângulo de inclinação do robô.
     *
     * @param value O valor do ângulo de inclinação desejado.
     */
    fun follow() {
        temiRobot.beWithMe()
    }

    /**
     * Faz com que o robô pare de seguir o usuário.
     */
    fun unfollow() {
        temiRobot.stopMovement()
    }

    /**
     * Emite voz a partir do robô.
     *
     * @param text O texto a ser pronunciado pelo robô.
     */
    fun speech(text: String) {
        temiRobot.speak(TtsRequest.create(text, false))
    }
}