package com.robotec.temiwebsocket

import android.app.Activity
import android.content.Context
import android.widget.VideoView
import com.robotec.caller.follow.FollowMe
import com.robotec.caller.listener.Status
import com.robotec.caller.movements.Movements
import com.robotec.caller.navigation.Navigation

import com.robotec.caller.speak.Voice
import com.robotec.caller.config.Param
import com.robotec.caller.navigation.Maps
import org.json.JSONObject
import android.os.Handler
import android.widget.Toast
import com.robotec.caller.utils.VideoHelper


open class SocketAdapter(private var context: Context, videoView: VideoView) {
    private val voice = Voice()
    private val navigation = Navigation()
    private val follow = FollowMe()
    private val movements = Movements()
    private val listener = Status
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
            val talkFaceId = this.context.getResources()
                .getIdentifier(
                    JSONObject(data).getString("talkFace"),
                    "raw",
                    this.context.getPackageName()
                );
            val idleFaceId = this.context.getResources()
                .getIdentifier(
                    JSONObject(data).getString("idleFace"),
                    "raw",
                    this.context.getPackageName()
                );
            val text = JSONObject(data).getString("text")
            voice.startSpeak(text, true, context) {
                (context as? Activity)?.runOnUiThread() {
                    if (talkFaceId != 0) {
                        (context as? Activity)?.runOnUiThread() {
                            videoHelper.playVideo(videoView, talkFaceId)
                        }
                    }
                }
            }
            voice.finishWithoutSpeak(true, context) {
                (context as? Activity)?.runOnUiThread() {
                    if (idleFaceId != 0) {
                        (context as? Activity)?.runOnUiThread() {
                            videoHelper.playVideo(videoView, idleFaceId)
                        }
                    }
                    callbacktxt(guid)
                }
            }
        }
        if (action == "WakeUp") {
            voice.wakeUp(true, context) {
                callbacktxt(guid)
            }
        }
        if (action == "GoTo") {
            navigation.goTo(data, true, context) {
                callbacktxt(guid)
            }
        }
        if (action == "Follow") {
            follow.follow(true) {}
        }

        if (action == "Repose") {
            navigation.reposicionar {
                callbacktxt(guid)
            }
        }

        if (action == "Move") {
            val dist_x = JSONObject(data).getString("x").toFloat()
            val dist_y = JSONObject(data).getString("y").toFloat()
            val times = JSONObject(data).getString("times").toInt()
            var completedMovements = 0
            repeat(times) {
                movements.moveRobot(dist_x, dist_y) {
                    completedMovements++
                    if (completedMovements == times - 1) {
                        callbacktxt(guid)
                    }
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
            val video = this.context.getResources()
                .getIdentifier(data, "raw", this.context.getPackageName());
            if (video == 0) {
                callbacktxt(guid)
                return
            }
            (context as? Activity)?.runOnUiThread() {
                videoHelper.playVideo(videoView, video)
                callbacktxt(guid)
            }
        }

        if (action == "PlayAudio") {
            val audio = this.context.getResources()
                .getIdentifier(JSONObject(data).getString("audioId"), "raw", this.context.getPackageName());
            if (audio == 0) {
                callbacktxt(guid)
                return
            }

            val talkFaceId = this.context.getResources()
                .getIdentifier(
                    JSONObject(data).getString("talkFace"),
                    "raw",
                    this.context.getPackageName()
                );

            val idleFaceId = this.context.getResources()
                .getIdentifier(JSONObject(data).getString("idleFace"), "raw", this.context.getPackageName());
            if (talkFaceId != 0) {
                (context as? Activity)?.runOnUiThread() {
                    videoHelper.playVideo(videoView, talkFaceId)
                }
            }

            videoHelper.playAudio(audio) {
                (context as? Activity)?.runOnUiThread() {
                    if(idleFaceId != 0) {
                        videoHelper.playVideo(videoView, idleFaceId)
                    }
                    callbacktxt(guid)
                }
            }
        }

        if (action == "PlayVideo") {
            val video = this.context.getResources()
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
