package com.robotec.caller.follow

import android.util.Log
import com.robotec.caller.utils.Request
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener

class FollowMe {

    private var temiRobot: Robot = Robot.getInstance()
    private val request = Request()

    fun follow(onComplete: () -> Unit) {
        try {
            temiRobot.beWithMe()
            val followStatus = object : OnBeWithMeStatusChangedListener {
                override fun onBeWithMeStatusChanged(
                      status: String,
                ) {
                    Log.d("TemiCaller", "Status follow: $status")
                    if (status == "abort") {
                        Log.d("TemiCaller", "Follow abortado")
                        temiRobot.removeOnBeWithMeStatusChangedListener(this)
                        onComplete.invoke()
                    }
                }
            }
            temiRobot.addOnBeWithMeStatusChangedListener(followStatus)

        } catch (e: Exception) {
              Log.e("TemiCaller", "Erro ao seguir [Me]")
              onComplete.invoke()
        }
    }

    fun followRestricted(onComplete: () -> Unit) {
        try {
            temiRobot.constraintBeWith()
            val followRestrictedStatus = object : OnConstraintBeWithStatusChangedListener {
                override fun onConstraintBeWithStatusChanged(
                      isConstraint: Boolean,
                ) {
                    Log.d("TemiCaller", "Status follow restrito: $isConstraint")
                    if (isConstraint == false) {
                          Log.d("TemiCaller", "Follow abortado")
                          temiRobot.removeOnConstraintBeWithStatusChangedListener(this)
                          onComplete.invoke()
                    }

                }
            }
            temiRobot.addOnConstraintBeWithStatusChangedListener(followRestrictedStatus)
        } catch (e: Exception) {
              Log.e("TemiCaller", "Erro ao seguir [Restrito]")
              onComplete.invoke()
        }
    }
}
