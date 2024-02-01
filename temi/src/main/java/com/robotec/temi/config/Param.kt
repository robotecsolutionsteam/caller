package com.robotec.temi.config

import com.robotec.temi.utils.Request
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

}