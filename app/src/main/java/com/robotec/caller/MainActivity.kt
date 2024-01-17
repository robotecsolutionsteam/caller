package com.robotec.caller

import android.os.Bundle
import com.robotec.caller.R
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.databinding.ActivityMainBinding
import com.robotec.caller.utils.CardAdapter
import com.robotec.caller.utils.Cardboard

class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var cardboardRecycler: RecyclerView
    private lateinit var cardboardList: ArrayList<Cardboard>
    private lateinit var imageViewID: ArrayList<Int>
    private lateinit var textViewID: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: images
        imageViewID = arrayListOf(
            R.drawable.navigation,
            R.drawable.voice,
        )

        textViewID = arrayListOf(
            "NAVIGATION",
            "VOICE",
        )

        cardboardRecycler = findViewById(R.id.recycler_view)
        cardboardRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cardboardList = arrayListOf<Cardboard>()
        getUserData()
    }

    private fun getUserData(){
        for(i in imageViewID.indices){
            val cardboard = Cardboard(imageViewID[i], textViewID[i])
            cardboardList.add(cardboard)
        }

        cardboardRecycler.adapter = CardAdapter(cardboardList)
    }
}