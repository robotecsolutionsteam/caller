package com.robotec.caller

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivityMainBinding
import com.robotec.temi.Commands

class MainActivity : ComponentActivity(), View.OnClickListener{

    private lateinit var binding: ActivityMainBinding

    private val commands = Commands()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acesso aos elementos de interface via 'binding'
        binding.buttonSpeak.setOnClickListener(this)
        binding.buttonGoto.setOnClickListener(this)
        binding.buttonFollow.setOnClickListener(this)
        binding.buttonTurn.setOnClickListener(this)
        binding.buttonAngle.setOnClickListener(this)
    }

    override fun onClick(view: View) {

            when (view.id) {
                R.id.button_speak -> {
                    commands.speech("Bora familia")
                }
                R.id.button_goto -> {
                    commands.goto("sofa")
                }
                R.id.button_follow -> {
                    commands.follow()
                }
                R.id.button_angle -> {
                    commands.angle(90)
                }
                R.id.button_turn -> {
                    commands.turnBy(60)
                }
                R.id.button_unfollow -> {
                    commands.unfollow()
                }
            }
        }
    }