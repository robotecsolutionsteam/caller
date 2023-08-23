package com.robotec.temi.utils

import android.util.Log
import com.robotemi.sdk.Robot
import java.util.concurrent.Semaphore
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener

class Listener: OnGoToLocationStatusChangedListener {

    private var temiRobot: Robot

    private var gotoStatus = false

    init {
        temiRobot = Robot.getInstance()
        temiRobot.addOnGoToLocationStatusChangedListener(this)
    }

    fun gotoCallback(): Boolean {
        //while (!gotoStatus) {
        //    Log.i("While", "Esperando ...")
        //}
        gotoStatus = false
        return true
        }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        Log.i("Listener", "Comeco")
        if (description.isNotBlank()) {
            if (status == "complete") {
                gotoStatus = true
                Log.i("Listener", "Status: $gotoStatus")

            }
        }
    }
}