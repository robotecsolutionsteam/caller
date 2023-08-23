package com.robotec.temi

import android.util.Log
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.map.MapModel
import com.robotec.temi.utils.Request
import com.robotec.temi.utils.Listener
import com.robotemi.sdk.constants.CliffSensorMode
import com.robotemi.sdk.constants.Platform
import com.robotemi.sdk.navigation.model.Position
import com.robotemi.sdk.navigation.model.SpeedLevel
import com.robotemi.sdk.telepresence.Participant

import com.robotemi.sdk.constants.Mode
import com.robotemi.sdk.constants.SensitivityLevel

class Commands {

    private var temiRobot: Robot = Robot.getInstance()
    private var mapList: List<MapModel> = ArrayList()

    private val listener = Listener()
    private val request = Request()

    init {
        // Permission
        request.requestToBeKioskApp()
    }

    // ######### FOLLOW ######### //

    fun follow() {
        try {
            Log.d("Follow", "Seguindo")
            temiRobot.beWithMe()
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao seguir")
        }
    }

    fun unfollow() {
        try {
            Log.d("Follow", "Parou de seguir")
            temiRobot.stopMovement()
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao parar de seguir")
        }
    }

    fun followRestricted() {
        try {
            Log.d("Follow", "Follow Restrito")
            temiRobot.constraintBeWith()
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao seguir restrito")
        }
    }

    // ######### COMMANDS ######### //

    fun joy(x: Int, y: Int, smart: Boolean) {
        try {
            val xFloat = x.toFloat()
            val yFloat = y.toFloat()
            Log.d("Commands", "X: $x, Y: $y, Smart: $smart")
            temiRobot.skidJoy(xFloat, yFloat, smart)
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao mover o robô")
        }
    }

    fun stop() {
        try {
            Log.d("Commands", "Parou")
            temiRobot.stopMovement()
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao parar")
        }
    }

    fun turnBy(value: Int) {
        try {
            Log.d("Commands", "Giro: $value")
            temiRobot.turnBy(value)
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao girar")
        }
    }

    fun angle(value: Int) {
        try {
            Log.d("Commands", "Angulo: $value")
            temiRobot.tiltAngle(value)
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao inclinar")
        }
    }

    fun tiltBy(degrees: Int, speed: Float) {
        try {
            Log.i("Commands", "Inclinação: $degrees, Velocidade: $speed")
            temiRobot.tiltBy(degrees, speed)
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao inclinar")
        }
    }

    fun getContatcs(): List<String> {
        val contactStrings = mutableListOf<String>()

        try {
            val allContacts = temiRobot.allContact
            Log.d("Commands", "Contatos: $allContacts")
            for (userInfo in allContacts) {
                val name = userInfo.name
                contactStrings.add(name)
            }

            return contactStrings
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao listar os contatos")
            return emptyList()
        }
    }

    fun telepresence() {
        // setSoundMode
        try {
            val target = temiRobot.adminInfo
            if (target == null) {
                Log.e("Commands", "target is null.")
                return
            }
            Log.i("Commands", "Contato: $target")
            temiRobot.startTelepresence(target.name, target.userId, Platform.MOBILE)
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao iniciar a telepresença")
        }
    }

    fun stopTelepresenca() {
        temiRobot.stopTelepresence()
    }

    fun meeting() {
        try {
            val target = temiRobot.adminInfo
            if (target == null) {
                Log.e("Commands", "target is null.")
                return
            }
            val resp = temiRobot.startMeeting(
                listOf(
                    Participant(target.userId, Platform.MOBILE),
                ), firstParticipantJoinedAsHost = true
            )
            Log.d("Commands", "Resultado da reunião $resp")
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao iniciar a reunião")
        }
    }

    fun getSerial(): String {
        try {
            val serial = temiRobot.serialNumber
            Log.d("Commands", "Serial: $serial")
            return serial.toString()
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao obter o serial")
            return ""
        }
    }

    fun Mode(mode: String) {
        try {
            request.requestToBeKioskApp()

            if (mode == "default") {
                temiRobot.setMode(Mode.DEFAULT)
                Log.d("Commands", "Modo: $mode")
            }

            if (mode == "greet") {
                temiRobot.setMode(Mode.GREET)
                Log.d("Commands", "Modo: $mode")
            }

            if (mode == "privacy") {
                temiRobot.setMode(Mode.PRIVACY)
                Log.d("Commands", "Modo: $mode")
            }

        } catch (e: Exception) {
            Log.e("Commands", "Erro ao solicitar o modo")
        }
    }

    fun sensors(baseDepth: Boolean, BaseSensitivity: Int, headDepth: Int, frontTOFE: Boolean, backTOFE: Boolean) {
        try {
            request.requestToBeKioskApp()

            if (baseDepth) {
                temiRobot.groundDepthCliffDetectionEnabled =
                    !temiRobot.groundDepthCliffDetectionEnabled
                Log.i("Commands", "Base Depth: Ativado")
            }
            if (!baseDepth) {
                temiRobot.groundDepthCliffDetectionEnabled =
                    temiRobot.groundDepthCliffDetectionEnabled
                Log.i("Commands", "Base Depth: Inativo")
            }

            if (BaseSensitivity == 0) {
                temiRobot.cliffSensorMode = CliffSensorMode.OFF
                Log.i("Commands", "Sensibilidade Base: OFF")
            }

            if (BaseSensitivity == 1) {
                temiRobot.cliffSensorMode = CliffSensorMode.LOW_SENSITIVITY
                Log.i("Commands", "Sensibilidade Base: LOW")
            }

            if (BaseSensitivity == 2) {
                temiRobot.cliffSensorMode = CliffSensorMode.HIGH_SENSITIVITY
                Log.i("Commands", "Sensibilidade Base: HIGH")
            }

            if (headDepth == 1) {
                temiRobot.headDepthSensitivity = SensitivityLevel.LOW
                Log.i("Commands", "Sensibilidade Cabeca: LOW")
            }

            if (headDepth == 2) {
                temiRobot.headDepthSensitivity = SensitivityLevel.HIGH
                Log.i("Commands", "Sensibilidade Cabeca: HIGH")
            }

            if (backTOFE) {
                temiRobot.backTOFEnabled = !temiRobot.backTOFEnabled
                Log.i("Commands", "TOFE traseira: Ativado")
            }

            if (!backTOFE) {
                temiRobot.backTOFEnabled = temiRobot.backTOFEnabled
                Log.i("Commands", "TOFE traseira: Inativado")
            }

            if (frontTOFE) {
                temiRobot.frontTOFEnabled = !temiRobot.frontTOFEnabled
                Log.i("Commands", "TOFE traseira: Ativado")
            }

            if (!frontTOFE) {
                temiRobot.frontTOFEnabled = temiRobot.frontTOFEnabled
                Log.i("Commands", "TOFE traseira: Inativado")
            }

        } catch (e: Exception) {
            Log.e("Commands", "Erro ao solicitar os sensores")
        }

    }

    fun getBattery(): Int {
        try {
            val batteryData = temiRobot.batteryData
            if (batteryData != null) {
                Log.d("Commands", "Bateria: ${batteryData.level.toString()}")
                return batteryData.level.toInt()
            }
        } catch (e: Exception) {
            Log.e("Commands", "Erro ao obter a bateria")
            return 0
        }
        return 0
    }

    fun setupSettings(
        hideTopBar: Boolean,
        privacyMode: Boolean,
        isHardButtons: Boolean,
        wakeup: Boolean,
        navBillboard: Boolean,
        topBadge: Boolean,
        returnOn: Boolean,
        setvolume: Int,
        muteAlexa: Boolean) {

        try {
            request.requestToBeKioskApp()
            request.requestSettings()

            // config 1
            if (hideTopBar) {
                temiRobot.hideTopBar()
                Log.i("Commands", "Escondeu a barra superior")
            }
            // config 2
            if (privacyMode) {
                temiRobot.privacyMode = true
                Log.i("Commands", "Modo privacidade: Ativado")
            }
            if (!privacyMode) {
                temiRobot.privacyMode = false
                Log.i("Commands", "Modo privacidade: Inativo")
            }
            // config 3
            if (isHardButtons) {
                temiRobot.isHardButtonsDisabled = true
                Log.i("Commands", "Botões físicos: Inativo")
            }
            if (!isHardButtons) {
                temiRobot.isHardButtonsDisabled = false
                Log.i("Commands", "Botões físicos: Ativado")
            }
            if (wakeup) {
                temiRobot.toggleWakeup(true)
                Log.i("Commands", "Wakeup: Inativo")
            }
            if (!wakeup) {
                temiRobot.toggleWakeup(false)
                Log.i("Commands", "Wakeup: Ativado")
            }
            if (navBillboard) {
                temiRobot.toggleNavigationBillboard(!temiRobot.navigationBillboardDisabled)
                Log.i("Commands", "Billboard: Inativo")
            }
            if (!navBillboard) {
                temiRobot.toggleNavigationBillboard(temiRobot.navigationBillboardDisabled)
                Log.i("Commands", "Billboard: Ativado")
            }

            if (topBadge) {
                temiRobot.topBadgeEnabled = false
                Log.i("Commands", "Top badge: Inativo")
            }

            if (!topBadge) {
                temiRobot.topBadgeEnabled = true
                Log.i("Commands", "Top badge: Ativado")
            }

            if (returnOn) {
                temiRobot.autoReturnOn = false
                Log.i("Commands", "Auto return: Inativo")
            }

            if (!returnOn) {
                temiRobot.autoReturnOn = true
                Log.i("Commands", "Auto return: Ativado")
            }

            temiRobot.volume = setvolume
            Log.d("Commands", "Volume: $setvolume")

            if (muteAlexa) {
                temiRobot.muteAlexa()
                Log.i("Commands", "Alexa: Inativo")
            }

        } catch (e: Exception) {
            Log.e("Commands", "Erro ao configurar as definições")
        }
    }

    fun desligar() {
        request.requestToBeKioskApp()

        temiRobot.shutdown()
    }

    // ######### NAVIGATION ######### //

    fun deleteLocal(local: String) {
        try { 
            val location = local.toString().lowercase().trim { it <= ' ' }
            Log.d("Navigation", "Local: $location")
            temiRobot.deleteLocation(location)
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao deletar o local")
        }
    }

    fun saveLocal(local: String) {
        try { 
            val location = local.toString().lowercase().trim { it <= ' ' }
            val result = temiRobot.saveLocation(location)
            if (result) {
                Log.d("Navigation", "Local {$location} salvo com sucesso")
            } else {
                Log.d("Navigation", "Não foi possivel salvar o local {$location}")
            }
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao salvar o local")
        }
    }

    fun goto(local: String, paraTras: Boolean, semDesvio: Boolean, nivelVelocidade: Int): Boolean {
        // setNavigationSafety()
        // setMinimumObstacleDistance
        // setGoToSpeed
        try { 
            val location = local.toString().lowercase().trim { it <= ' ' }
            if (!temiRobot.locations.contains(location)) {
                Log.d("Navigation", "Local $location não existe")
                return false
            }

            if (nivelVelocidade == 3) {
                temiRobot.goTo(location, paraTras, semDesvio, SpeedLevel.HIGH)
                Log.i("Navigation", "Local: $location; Velocidade: Alta")
            }

            if (nivelVelocidade == 2) {
                temiRobot.goTo(location, paraTras, semDesvio, SpeedLevel.MEDIUM)
                Log.i("Navigation", "Local: $location; Velocidade: Média")
                Log.w("Navigation", "Antes")
            }

            if (nivelVelocidade == 1) {
                temiRobot.goTo(location, paraTras, semDesvio, SpeedLevel.SLOW)
                Log.i("Navigation", "Local: $location; Velocidade: Baixa")
            }
            
            return true

        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao ir para o local")
            return false
        }
    }

    fun getLocals(): List<String> {
        try { 
            val locals = temiRobot.locations

            Log.d("Navigation", "Locais: $locals")
            return locals

        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao listar os locais")
            return emptyList()
        }
    }

    fun goToPosition(x: Int, y: Int, yaw: Int, paraTras: Boolean, semDesvio: Boolean, nivelVelocidade: Int) {
        try {
            val xFloat = x.toFloat()
            val yFloat = y.toFloat()
            val yawFloat = yaw.toFloat()
            Log.d("Navigation", "X: $x, Y: $y, Yaw: $yaw")
            if (nivelVelocidade == 3) {
                temiRobot.goTo(Position(xFloat, yFloat, yawFloat, 0).toString(), paraTras, semDesvio, SpeedLevel.HIGH)
                Log.d("Navigation", "Velocidade: Alta")
            }
            if (nivelVelocidade == 2) {
                temiRobot.goTo(Position(xFloat, yFloat, yawFloat, 0).toString(), paraTras, semDesvio, SpeedLevel.MEDIUM)
                Log.d("Navigation", "Velocidade: Média")
            }
            if (nivelVelocidade == 1) {
                temiRobot.goTo(Position(xFloat, yFloat, yawFloat, 0).toString(), paraTras, semDesvio, SpeedLevel.SLOW)
                Log.d("Navigation", "Velocidade: Baixa")
            }
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao ir para a posição")
        }
    }

    fun repose() {
        try {
            Log.d("Navigation", "Robô se reposicionando")
            temiRobot.repose()
        } catch (e: Exception) {
            Log.e("Navigation", "Erro ao reposicionar o robô")
        }
    }

    fun patrol(locais: List<String>, semParar: Boolean, time: Int, waiting: Int) {
        TODO()
    }

    // ######### MAP ######### //

    fun getMaps(): List<String> {
        try {
            request.requestMaps()

            val mapList = temiRobot.getMapList()

            val mapListString: MutableList<String> = ArrayList()
            for (i in mapList.indices) {
                mapListString.add(mapList[i].name)
            }

            Log.d("Map", "Mapas: $mapListString")
            return mapListString

        } catch (e: Exception) {
            Log.e("Map", "Erro ao listar os mapas")
            return emptyList()
        }
    }

    fun loadMap(selectedMap: String, reposeRequired: Boolean, x: Int, y: Int, yaw: Int, position: Boolean, offline: Boolean) {
        try {
            val mapListString: MutableList<String> = ArrayList()
            val mapListStringId: MutableList<String> = ArrayList()

            mapList = temiRobot.getMapList()

            val xFloat = x.toFloat()
            val yFloat = y.toFloat()
            val yawFloat = yaw.toFloat()

            for (i in mapList.indices) {
                mapListString.add(mapList[i].name)
                mapListStringId.add(mapList[i].id)
            }

            val mapaIdMap = mapListString.zip(mapListStringId).toMap()
            val mapId = mapaIdMap[selectedMap]
            Log.i("Map", "O mapa selecionado foi $selectedMap")

            if (mapId != null) {
                if (position) {
                    Log.d("Map", "O mapa selecionado foi $selectedMap")
                    temiRobot.loadMap(
                        mapId,
                        reposeRequired,
                        Position(xFloat, yFloat, yawFloat, 0),
                        offline,
                        withoutUI=true
                    )
                }
                if (!position) {
                    Log.d("Map", "O mapa selecionado foi $selectedMap")
                    temiRobot.loadMap(
                        mapId,
                        reposeRequired,
                        position=null,
                        offline,
                        withoutUI=true
                    )
                }
            } else {
                Log.e("Map", "ID não encontrado para o mapa selecionado.")
            }
        } catch (e: Exception) {
            Log.e("Map", "Erro ao carregar o mapa $selectedMap")
        }
    }

    // ######### SPEAK ######### //

    fun speak(text: String) {
        try {
            Log.d("Speak", "Texto: $text")
            temiRobot.speak(TtsRequest.create(text, false))
        } catch (e: Exception) {
            Log.e("Speak", "Erro ao falar")
        }
    }

    fun cancelSpeak() {
        try {
            temiRobot.cancelAllTtsRequests()
            Log.d("Speak", "Cancelou o texto")
        } catch (e: Exception) {
            Log.e("Speak", "Erro ao cancelar o texto")
        }
    }

    // Perguntar uma frase e obter uma resposta
    // ouvir uma frase e obter uma resposta
    // Colcoar um texto e objeto uma acao mais proxima

    fun askQuestion(text: String): String {
        TODO()
    }

    fun asr(time: Int): String {
        TODO()
    }

    fun trigger(text: String): Boolean {
        TODO()
    }

    fun setupSpeak() {
        TODO()
    }
}