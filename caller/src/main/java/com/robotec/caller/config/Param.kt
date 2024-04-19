package com.robotec.caller.config

import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot

class Param {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    enum class SafetyLevel {
      HIGH,
      MEDIUM;
   }

    fun setSafetyMode() {
        temiRobot.navigationSafety
    }

    fun getSerialNumber(): String? {
        return temiRobot.serialNumber
    }
}