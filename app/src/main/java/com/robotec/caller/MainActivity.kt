package com.robotec.caller

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.robotec.temi.Commands

class MainActivity : ComponentActivity() {

    // instance of Commands class
    private val commands = Commands()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // call goto() method from Commands class
        commands.goto("sofa")

        commands.angle(90)
        commands.turnBy(60)
        commands.follow()
        commands.speech("Bora familia")
        commands.unfollow()
    }
}