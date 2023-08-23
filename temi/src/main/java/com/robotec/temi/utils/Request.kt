package com.robotec.temi.utils

import android.util.Log
import com.robotemi.sdk.Robot
import androidx.annotation.CheckResult
import com.robotemi.sdk.permission.Permission

private const val REQUEST_CODE_NORMAL = 0
private const val REQUEST_EXTERNAL_STORAGE = 1
private const val REQUEST_CODE_FACE_START = 1
private const val REQUEST_CODE_FACE_STOP = 2
private const val REQUEST_CODE_MAP = 3
private const val REQUEST_CODE_SEQUENCE_FETCH_ALL = 4
private const val REQUEST_CODE_SEQUENCE_PLAY = 5
private const val REQUEST_CODE_START_DETECTION_WITH_DISTANCE = 6
private const val REQUEST_CODE_SEQUENCE_PLAY_WITHOUT_PLAYER = 7
private const val REQUEST_CODE_GET_MAP_LIST = 8
private const val REQUEST_CODE_GET_ALL_FLOORS = 9

class Request {

    private var temiRobot: Robot = Robot.getInstance()

    init {

    }

    fun requestSettings() {
        if (requestPermissionIfNeeded(Permission.SETTINGS, REQUEST_CODE_NORMAL)) {
            Log.w("Permission", "Não tem permissão para acessar as configurações")
            return
        }

        temiRobot.requestPermissions(listOf(Permission.SETTINGS), REQUEST_CODE_NORMAL)
        Log.i("Permission", "Permissão para acessar as configurações")
    }

    fun requestMaps() {
        if (requestPermissionIfNeeded(Permission.MAP, REQUEST_CODE_GET_MAP_LIST)) {
            Log.w("Permission", "Não tem permissão para acessar os mapas")
            return
        }

        temiRobot.requestPermissions(listOf(Permission.MAP), REQUEST_CODE_GET_MAP_LIST)
        Log.i("Permission", "Permissão para acessar os mapas")
    }


    @CheckResult
    fun requestPermissionIfNeeded(permission: Permission, requestCode: Int): Boolean {
        if (temiRobot.checkSelfPermission(permission) == Permission.GRANTED) {
            return false
        }

        temiRobot.requestPermissions(listOf(permission), requestCode)
        return true
    }

    fun requestToBeKioskApp() {
        if (temiRobot.isSelectedKioskApp()) {
            Log.d("Permission", "Kiosk Ativado")
            return
        }
        temiRobot.requestToBeKioskApp()
    }
}