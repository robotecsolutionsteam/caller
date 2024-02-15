package com.robotec.caller

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivityMainBinding

import com.robotec.caller.bluetooth.Bluetooth

class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding
    private val bluetoothInstance = Bluetooth()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothInstance.setContext(this, "FC:B4:67:50:FD:A6")

        // iniciar o serviço do bluetooth
        bluetoothInstance.startBluetoothService()

        // mandar mensagem com o bluetooth
        bluetoothInstance.send("oii")

    }
}