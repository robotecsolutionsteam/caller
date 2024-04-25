package com.robotec.caller

import android.util.Log
import android.os.Bundle
import android.os.Handler
import android.widget.AdapterView
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivitySampleBinding

import com.robotec.caller.speak.Voice
import com.robotec.caller.follow.FollowMe
import com.robotec.caller.utils.Config
import com.robotec.caller.utils.Network

import com.robotec.caller.listener.Status
import com.robotec.caller.navigation.Navigation
import com.robotec.caller.utils.GridAdapter


class SampleActivity : ComponentActivity(){

    private lateinit var binding: ActivitySampleBinding

    private val navigation = Navigation()
    private val followMe = FollowMe()
    private val config = Config()
    private val speak = Voice()
    private val net = Network


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val features = arrayOf(
            "follow",
            "followRestricted",
            "simpleSpeak",
            "finishSpeak",
            "finishWithoutSpeak",
            "block",
            "wakeUp",
            "stopSpeak",
            "goTo",
            "returnBase",
            "goToDistance",
            "network"
        )

        val gridAdapter = GridAdapter(this@SampleActivity, features)
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
                speak.startSpeak("Olá, planeta Terra!", true, this) {
                    Log.v("Robot",  Status.currentSpeakStatus)
                }
            }

            if (position == 3) {
                Log.i("Robot", "Iniciando [finishSpeak] ...")
                speak.finishSpeak("Teste de voz.", false, this) {
                    Log.v("Robot",  Status.currentSpeakStatus)
                }
            }

            if (position == 4) {
                Log.i("Robot", "Iniciando [finishWithoutSpeak] ...")
                speak.finishWithoutSpeak(true, this) {
                    Log.v("Robot",  Status.currentSpeakStatus)
                }
            }

            if (position == 5) {
                Log.i("Robot", "Iniciando [block] ...")
                config.block(this, true)
            }

            if (position == 6) {
                Log.i("Robot", "Iniciando [wakeUp] ...")
                speak.wakeUp(true, this) {
                    Log.v("Robot",  Status.currentAsrStatus)
                }
            }

            if (position == 7)  {
                Log.i("Robot", "Iniciando [stopSpeak] ...")
                speak.stopSpeak(true, this)
            }

            if (position == 8)  {
                Log.i("Robot", "Iniciando [stopSpeak] ...")
                navigation.goTo("1", true, this) {
                    Log.v("Robot",  Status.currentNavigationStatus)

                }
            }

            if (position == 9)  {
                Log.i("Robot", "Iniciando [stopSpeak] ...")
                navigation.returnBase(true, this) {
                    Log.v("Robot",  Status.currentNavigationStatus)
                }
            }

            if (position == 10) {
                Log.i("Robot", "Iniciando [goToDistance] ...")
                navigation.goToDistance(true, this) {
                    Log.v("Robot",  Status.currentDistanceStatus.toString())
                }
            }

            if (position == 11) {
                Log.i("Robot", "Iniciando [network] ...")
                net.startWifiCheck(this)
            }
        }
    }

    fun processarLegenda(legenda: String) {
        // Aqui você pode implementar a lógica para processar a legenda,
        // como reproduzir a fala com o tempo correspondente.
        // Por enquanto, vou apenas imprimir a legenda para fins de demonstração.
        println("Processando legenda: $legenda")
    }
}