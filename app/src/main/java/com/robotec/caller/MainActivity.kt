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
        binding.test1.setOnClickListener(this)
        binding.test2.setOnClickListener(this)
        binding.test3.setOnClickListener(this)
        binding.test4.setOnClickListener(this)
        binding.test5.setOnClickListener(this)
        binding.test6.setOnClickListener(this)
        binding.test7.setOnClickListener(this)
        binding.test8.setOnClickListener(this)

    }

    override fun onClick(view: View) {

            when (view.id) {
                R.id.test1 -> {
                    commands.enableBillboard()
                }
                R.id.test2 -> {
                    commands.deleteLocal("sofa")
                }
                R.id.test3 -> {
                    commands.saveLocal("test5")
                }
                R.id.test4 -> {
                    val speed: Float = 0.5f
                    commands.tiltBy(45, speed)
                }
                R.id.test5 -> {
                    commands.locals()
                }
                R.id.test6 -> {
                    commands.goToPosition(1, 2, 4, false, false, 2)
                }
                R.id.test7 -> {
                    val locations = listOf("test5", "sofa", "cafe")
                    commands.patrula(locations, false, 0, 3)
                }
                R.id.test8 -> {
                    commands.loadMap("quintoandar", false, 0, 0, 0, false, false)
                }
            }
        }
    }