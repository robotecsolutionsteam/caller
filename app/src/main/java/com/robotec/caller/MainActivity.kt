package com.robotec.caller

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.robotec.temi.Hello

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Hello.showToast(this, "Olá do HelloLibrary!")
    }
}