package com.robotec.caller.view

import android.os.Bundle
import com.robotec.caller.R
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.databinding.ActivityNavigationBinding
import com.robotec.caller.utils.speak.Speak
import com.robotec.caller.utils.speak.SpeakAdapter

class SpeakActivity : ComponentActivity(){

    private lateinit var binding: ActivityNavigationBinding

    private lateinit var featureRecycler: RecyclerView
    private lateinit var featureList: ArrayList<Speak>
    private lateinit var imageViewID: ArrayList<Int>
    private lateinit var labelViewID: ArrayList<String>
    private lateinit var tittleViewID: ArrayList<String>
    private lateinit var descriptionViewID: ArrayList<String>
    private lateinit var paramViewID: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageViewID = arrayListOf(
            R.drawable.voice,
        )

        labelViewID = arrayListOf(
            "Command",
        )

        tittleViewID = arrayListOf(
            "Speak",
        )

        descriptionViewID = arrayListOf(
            "Comando para falar",
        )

        paramViewID = arrayListOf(
            "Local",
        )

        featureRecycler = findViewById(R.id.recycler_view)
        featureRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        featureList = arrayListOf<Speak>()
        getUserData()
    }

    private fun getUserData(){
        for(i in imageViewID.indices){
            val feature = Speak(imageViewID[i], labelViewID[i], tittleViewID[i], descriptionViewID[i], paramViewID[i], "")
            featureList.add(feature)
        }

        featureRecycler.adapter = SpeakAdapter(featureList)
    }
}