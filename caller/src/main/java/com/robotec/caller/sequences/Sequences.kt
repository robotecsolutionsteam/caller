package com.robotec.caller.sequences

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.sequence.SequenceModel
import com.robotemi.sdk.permission.Permission

class Sequences {


    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    init {
        request.requestMaps()
    }

    fun getSequences(tag: String): List<SequenceModel> {
        temiRobot.requestPermissions(listOf(Permission.SEQUENCE), 8)
        return temiRobot.getAllSequences(listOf(tag))
    }

    fun startSequence(tag:String){
        val id = getSequences(tag)[0].id
        temiRobot.playSequence(id)
    }


}