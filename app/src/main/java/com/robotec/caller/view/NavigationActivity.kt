package com.robotec.caller.view

import android.os.Bundle
import com.robotec.caller.R
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.databinding.ActivityNavigationBinding
import com.robotec.caller.utils.navigation.Features
import com.robotec.caller.utils.navigation.FeaturesAdapter

class NavigationActivity : ComponentActivity(){

    private lateinit var binding: ActivityNavigationBinding

    private lateinit var featureRecycler: RecyclerView
    private lateinit var featureList: ArrayList<Features>
    private lateinit var imageViewID: ArrayList<Int>
    private lateinit var labelViewID: ArrayList<String>
    private lateinit var tittleViewID: ArrayList<String>
    private lateinit var descriptionViewID: ArrayList<String>
    private lateinit var paramViewID: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: images
        imageViewID = arrayListOf(
            R.drawable.navigation,
            R.drawable.logo,
        )

        labelViewID = arrayListOf(
            "Command",
            "Command",
        )

        tittleViewID = arrayListOf(
            "Go to",
            "Safety",
        )

        descriptionViewID = arrayListOf(
            "Comando para navegar até um local especifico",
            "Comando para navegar até um local especifico",
        )

        paramViewID = arrayListOf(
            "Local",
            "Local, ambiente",
        )

        featureRecycler = findViewById(R.id.recycler_view)
        featureRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        featureList = arrayListOf<Features>()
        getUserData()
    }

    private fun getUserData(){
        for(i in imageViewID.indices){
            val feature = Features(imageViewID[i], labelViewID[i], tittleViewID[i], descriptionViewID[i], paramViewID[i], "")
            featureList.add(feature)
        }

        featureRecycler.adapter = FeaturesAdapter(featureList)
    }
}