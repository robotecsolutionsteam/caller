package com.robotec.temi.utils

import android.util.Log
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener

class Listener {
    //OnGoToLocationStatusChangedListener {

    //private var temiRobot: Robot = Robot.getInstance()

    //private var gotoStatus = false

    //init {
    //    temiRobot.addOnGoToLocationStatusChangedListener(this)
    //}

    //fun gotoCallback(): Boolean {
    //    while (!gotoStatus) Thread.sleep(1000)
    //    gotoStatus = false
    //    Log.d("Command", "Status: $gotoStatus")
    //    return true
    //}

    //override fun onGoToLocationStatusChanged(
    //    location: String,
    //    status: String,
    //    descriptionId: Int,
    //    description: String
    //) {
    //    gotoStatus = when (status) {
    //        "complete" -> { true }
    //        else -> { false }
    //    }
    //}
}