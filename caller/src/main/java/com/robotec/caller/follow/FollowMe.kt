package com.robotec.caller.follow

import android.util.Log
import com.robotemi.sdk.Robot
import com.robotec.caller.listener.Status
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener

class FollowMe {

    private var temiRobot: Robot = Robot.getInstance()

    fun follow(useTemi: Boolean, onComplete: () -> Unit) {
        try {
            if (useTemi){
                temiRobot.beWithMe()

                val followStatus = object : OnBeWithMeStatusChangedListener {
                    override fun onBeWithMeStatusChanged(
                        status: String,
                    ) {
                        Status.currentFollowStatus = status
                        if (status == "abort") {
                            temiRobot.removeOnBeWithMeStatusChangedListener(this)
                            onComplete.invoke()
                        }
                    }
                }
                temiRobot.addOnBeWithMeStatusChangedListener(followStatus)

            } else {
                Log.v("TemiCaller", "Sem usar o Follow Me")
                onComplete.invoke()
            }

        } catch (e: Exception) {
              Log.e("TemiCaller", "Erro ao seguir [Me]")
              onComplete.invoke()
        }
    }

    fun followRestricted(useTemi: Boolean, onComplete: () -> Unit) {
        try {
            if (useTemi) {
                temiRobot.constraintBeWith()
                val followRestrictedStatus = object : OnConstraintBeWithStatusChangedListener {
                    override fun onConstraintBeWithStatusChanged(
                        isConstraint: Boolean,
                    ) {
                        Status.currentFollowRestrictedStatus = isConstraint.toString()
                        if (isConstraint == false) {
                            temiRobot.removeOnConstraintBeWithStatusChangedListener(this)
                            onComplete.invoke()
                        }
                    }
                }
                temiRobot.addOnConstraintBeWithStatusChangedListener(followRestrictedStatus)
            } else {
                Log.v("TemiCaller", "Sem usar o followRestricted")
                onComplete.invoke()
            }
        } catch (e: Exception) {
              Log.e("TemiCaller", "Erro ao seguir [Restrito]")
              onComplete.invoke()
        }
    }
}
