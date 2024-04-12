package com.robotec.caller.listener

object Status {
    var currentFollowRestrictedStatus: String = ""
    var currentFollowStatus: String = ""
    var currentNavigationStatus: String = ""
    var currentMovementStatus: String = ""
    var currentSpeakStatus: String = ""
    var currentDistanceStatus: Float = 0.toFloat()
    var currentAsrStatus: String = ""
    var currentDetectionStatus: Int = 0
    var currentRobotSpeed: Float = 0f
    var currentLiftedStatus: Boolean = false
}