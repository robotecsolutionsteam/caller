package com.robotec.caller

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivityMainBinding

// Importar o package Follow
import com.robotec.caller.follow.FollowMe

import com.robotec.caller.listener.Status


class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding

    // Inicializar o Follow
    private val followMe = FollowMe()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val features = arrayOf(
            "follow",
            "followRestricted"
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
                Log.i("Robot", "[followRestricted]")
                followMe.followRestricted(true) {
                    Log.v("Robot", "[followRestricted] finalizado!")
                }
            }
        }
    }
}