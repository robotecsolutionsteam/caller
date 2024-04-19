package com.robotec.caller.websocket

import android.app.Activity
import android.content.Context
import android.widget.VideoView
import com.robotec.caller.follow.FollowMe
import com.robotec.caller.listener.Status
import com.robotec.caller.movements.Movements
import com.robotec.caller.navigation.Navigation
import com.robotec.caller.sequences.Sequences
import com.robotec.caller.speak.Voice
import com.robotec.caller.config.Param
import com.robotec.caller.navigation.Maps
import org.json.JSONObject
import android.os.Handler
import android.widget.Toast
import com.robotec.caller.utils.VideoHelper


open class SocketAdapter(private var context: Context,private var useTemi: Boolean, videoView: VideoView) {
    private val voice = Voice()
    private val navigation = Navigation()
    private val follow = FollowMe()
    private val movements = Movements()
    private val listener = Status
    private val sequences = Sequences()
    private val videoView = videoView
    private val videoHelper = VideoHelper(context)
    private val config = Param()
    private val map = Maps()

    fun readCommand(command: String, callbacktxt: (String) -> Unit) {
        val json = JSONObject(command)
        val action = json.getString("TipoDoComando")
        var data = json.getString("Comando")
        var guid = json.getString("GuidAtividade")

        if (action == "Speak") {
            voice.finishSpeak(data, useTemi, context) {
                callbacktxt(guid)
            }
        }
        if (action == "WakeUp") {
            voice.wakeUp(useTemi, context) {
                callbacktxt(guid)
            }
        }
        if (action == "GoTo") {
            navigation.goTo(data,useTemi, context) {
                callbacktxt(guid)
            }
        }
        if (action == "Follow") {
            follow.follow(useTemi) {}
        }

        if (action == "Repose") {
            navigation.reposicionar {
                callbacktxt(guid) }
        }
        if (action == "StartSequence") {
            sequences.startSequence(data)
        }

        if (action == "Move") {
            val dist_x = JSONObject(data).getString("x").toFloat()
            val dist_y = JSONObject(data).getString("y").toFloat()
            val times = JSONObject(data).getString("times").toInt()
            repeat(times) {
                movements.moveRobot(dist_x, dist_y) {
                    callbacktxt(guid)
                }
            }
        }

        if (action == "Turn") {
            val angle = JSONObject(data).getString("angle").toInt()
            val speed = JSONObject(data).getString("speed").toFloat()
            movements.turnRobot(angle, speed) {
                callbacktxt(guid)
            }
        }

        if (action == "Tilt") {
            val angle = JSONObject(data).getString("angle").toInt()
            val speed = JSONObject(data).getString("speed").toFloat()
            movements.tiltHead(angle, speed) {
                callbacktxt(guid)
            }
        }
        if (action == "ChangeFace") {
            val video = this.context.resources
                .getIdentifier(data, "raw", this.context.getPackageName());
            if (video == 0) {
                callbacktxt(guid)
                return
            }
            (context as? Activity)?.runOnUiThread {
                videoHelper.playVideo(videoView, video)
                callbacktxt(guid)
            }
        }

        if (action == "TalkMp3") {
            val audio = this.context.resources
                .getIdentifier(data, "raw", this.context.getPackageName());
            if (audio == 0) {
                callbacktxt(guid)
                return
            }
            val talkFace = JSONObject(data).getString("talkFace")
            val talkFaceId = this.context.getResources()
                .getIdentifier(talkFace, "raw", this.context.getPackageName());
            val idleFace = JSONObject(data).getString("idleFace")
            val idleFaceId = this.context.getResources()
                .getIdentifier(idleFace, "raw", this.context.getPackageName());
            if (talkFace != "") {
                (context as? Activity)?.runOnUiThread() {
                    videoHelper.playVideo(videoView, talkFaceId)
                }
            }

            videoHelper.playAudio(audio) {
                (context as? Activity)?.runOnUiThread() {
                    videoHelper.playVideo(videoView, idleFaceId)
                    callbacktxt(guid)
                }
            }
        }

        if (action == "PlayVideoOnce") {
            val video = this.context.resources
                .getIdentifier(data, "raw", this.context.getPackageName());
            if (video == 0) {
                callbacktxt(guid)
                return
            }
            (context as? Activity)?.runOnUiThread() {
                videoHelper.playVideoOnce(videoView, video) {
                    callbacktxt(guid)
                }
            }
        }

        if (action == "GetSerialNumber") {
            callbacktxt("Status getSerialNumber: " + config.getSerialNumber())
        }

        if (action == "MapList") {
            callbacktxt(map.mapList {}.toString())
        }



    }
}
