package com.robotec.caller.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.widget.VideoView

class VideoHelper(private var context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var audioMediaPlayer: MediaPlayer? = null

    fun playVideo(videoView: VideoView, videoResourceId: Int) {


        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResourceId")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer = it }

        videoView.setOnCompletionListener {
            mediaPlayer?.start()
        }

        videoView.start()
    }

    fun stopVideo(videoView: VideoView) {
        if (videoView.isPlaying) {
            videoView.stopPlayback()
        }
    }

    private fun stopVideo() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playAudio(audioResourceId: Int,onComplete: () -> Unit){
        audioMediaPlayer = MediaPlayer.create(context,audioResourceId)
        audioMediaPlayer?.start()
        audioMediaPlayer?.setOnCompletionListener {
            onComplete.invoke()
        }
    }

    fun playVideoOnce(videoView: VideoView, videoResourceId: Int, onComplete: () -> Unit) {
        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResourceId")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener {
            mediaPlayer = it
            it.setOnCompletionListener {
                onComplete.invoke()
            }
        }
        videoView.start()
    }
}