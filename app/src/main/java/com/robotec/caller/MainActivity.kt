package com.robotec.caller

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.activity.ComponentActivity
import com.robotec.caller.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardName = arrayOf("goTo", "goTo", "goTo", "goTo", "goTo", "goTo", "goTo", "goTo", "goTo", "goTo")

        val gridAdapter = GridAdapter(this@MainActivity, cardName)
        binding.gridView.setAdapter(gridAdapter)

        binding.gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position == 1) {
                Log.d("Main", "goTo")
            }
        }
    }
}