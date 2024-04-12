package com.robotec.caller

import android.util.Log
import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivityMainBinding

import com.robotec.caller.speak.Voice
import com.robotec.caller.follow.FollowMe
import com.robotec.caller.utils.Config

import com.robotec.caller.listener.Status

class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding

    private val followMe = FollowMe()
    private val config = Config()
    private val speak = Voice()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val features = arrayOf(
            "follow",
            "followRestricted",
            "simpleSpeak",
            "finishSpeak",
            "block",
            "wakeUp",
            "stopSpeak"
        )

        val gridAdapter = GridAdapter(this@MainActivity, features)
        binding.gridView.setAdapter(gridAdapter)

        binding.gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                Log.i("Robot", "Iniciando [follow] ...")
                followMe.follow(true) {
                    if (Status.currentFollowStatus == "abort") {
                        Log.v("Robot", "Erro no follow.")
                    }
                }
            }

            if (position == 1) {
                Log.i("Robot", "Iniciando [followRestricted] ...")
                followMe.followRestricted(true) {
                    Log.v("Robot",  Status.currentFollowRestrictedStatus)
                }
            }

            if (position == 2) {
                Log.i("Robot", "Iniciando [simpleSpeak] ...")
                speak.startSpeak("Teste de voz.", true) {
                    Log.v("Robot",  Status.currentSpeakStatus)
                }
            }

            if (position == 3) {
                Log.i("Robot", "Iniciando [finishSpeak] ...")
                speak.finishSpeak("Teste de voz.", true) {
                    Log.v("Robot",  Status.currentSpeakStatus)
                }
            }

            if (position == 4) {
                Log.i("Robot", "Iniciando [block] ...")
                config.block(this)
            }

            if (position == 5) {
                Log.i("Robot", "Iniciando [wakeUp] ...")
                speak.wakeUp() {
                    Log.v("Robot",  Status.currentAsrStatus)
                }
            }

            if (position == 6)  {
                Log.i("Robot", "Iniciando [stopSpeak] ...")
                speak.stopSpeak()
            }
        }
    }
}