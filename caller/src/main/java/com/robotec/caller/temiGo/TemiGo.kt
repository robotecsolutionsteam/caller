package com.robotec.caller.temiGo
import android.util.Log
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnSerialRawDataListener
import com.robotemi.sdk.serial.Serial
import com.robotemi.sdk.serial.Serial.cmd
import com.robotemi.sdk.serial.Serial.dataFrame
import com.robotemi.sdk.serial.Serial.getLcdColorBytes
import com.robotemi.sdk.serial.Serial.weight

class TemiGo {

    private var temiRobot: Robot = Robot.getInstance()

    fun changeTrayLed(tray: Tray, color: Color) {
        if (color.red > 255 || color.green > 255 || color.blue > 255)
            Log.e("Tray Led", "Invalid color, must be between 0 and 255")
        temiRobot.sendSerialCommand(
            Serial.CMD_TRAY_LIGHT,
            byteArrayOf(tray.trayNumber.toByte(), color.red.toByte(), color.green.toByte(), color.blue.toByte())
        )
    }

    fun changeBaseLed(mode: Int, primaryColor: Color, secondaryColor: Color, interval: Int) {
        if (primaryColor.red > 255 || primaryColor.green > 255 || primaryColor.blue > 255)
            Log.e("Base Led", "Invalid primary color, must be between 0 and 255")
        if(secondaryColor.red > 255 || secondaryColor.green > 255 || secondaryColor.blue > 255)
            Log.e("Base Led", "Invalid secondary color, must be between 0 and 255")
        if(mode < 0 || mode > 3)
            Log.e("Base Led", "Invalid mode, must be between 0 and 3")

        temiRobot.sendSerialCommand(
            Serial.CMD_STRIP_LIGHT,
            Serial.getStripBytes(
                mode = mode,
                primaryColor = byteArrayOf(primaryColor.red.toByte(), primaryColor.green.toByte(), primaryColor.blue.toByte()),
                secondaryColor = byteArrayOf(secondaryColor.red.toByte(), secondaryColor.green.toByte(), secondaryColor.blue.toByte()),
                interval = interval
            )
        )
    }

    fun getTrayWeight(tray: Tray, callback: (Int?) -> Unit) {
        temiRobot.sendSerialCommand(Serial.CMD_TRAY_SENSOR, byteArrayOf(tray.trayNumber.toByte()))
        val listener = object : OnSerialRawDataListener{
            override fun onSerialRawData(data: ByteArray) {
                val cmd = data.cmd

                val dataFrame = data.dataFrame
                when (cmd) {
                    Serial.RESP_TRAY_SENSOR -> {
                        val trayIndex = dataFrame[0].toInt()
                        val trayNum = trayIndex

                        val weight = dataFrame.weight

                        if(trayNum == tray.trayNumber)
                        callback(weight)
                    }

                }
            }

        }
        temiRobot.addOnSerialRawDataListener(listener)
    }

    fun resetTraySensor(){
        temiRobot.sendSerialCommand(Serial.CMD_TRAY_CALIBRATE, byteArrayOf())
    }

    fun changeLcdText(text: String,textColor: Color,backgroundColor: Color) {
        temiRobot.sendSerialCommand(Serial.CMD_LCD_TEXT, Serial.getLcdBytes(text))
        temiRobot.sendSerialCommand(
            Serial.CMD_LCD_TEXT,
            getLcdColorBytes(byteArrayOf(textColor.red.toByte(), textColor.green.toByte(), textColor.blue.toByte()), target = Serial.LCD.TEXT_0_COLOR)
        )
        temiRobot.sendSerialCommand(
            Serial.CMD_LCD_TEXT,
            getLcdColorBytes(byteArrayOf(backgroundColor.red.toByte(), backgroundColor.green.toByte(), backgroundColor.blue.toByte()), target = Serial.LCD.TEXT_0_BACKGROUND)
        )
    }

    data class Color(
        val red: Int,
        val green: Int,
        val blue: Int
    )

}