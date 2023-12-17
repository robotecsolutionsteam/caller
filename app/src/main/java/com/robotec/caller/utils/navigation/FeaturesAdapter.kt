package com.robotec.caller.utils.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.R

import com.robotec.temi.navigation.Navigation
import com.robotec.temi.listener.Status

import com.robotec.temi.Speak

class FeaturesAdapter(private val featuresList : ArrayList<Features>) :
    RecyclerView.Adapter<FeaturesAdapter.FeaturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_list, parent, false)
        return FeaturesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeaturesViewHolder, position: Int) {

        val nav = Navigation()
        val speak = Speak()

        val currentItem = featuresList[position]
        holder.imageView.setBackgroundResource(currentItem.imageViewID)

        holder.labelView.text = currentItem.labelViewID
        holder.tittleView.text = currentItem.tittleViewID
        holder.descriptionView.text = currentItem.descriptionViewID
        holder.paramView.text = currentItem.paramViewID

        // ############################################################ //

        holder.buttonText.setOnClickListener {
            nav.goTo("x6") {
                val status = Status.currentNavigationStatus
                if (status == "complete") {
                    println("ebbaaaa")
                }

                if (status == "abort") {
                    println("bosta")
                }
            }
        }

        //if (position == 1) {
        //    holder.buttonText.setOnClickListener {
        //        speak.wakeUp("Oi Temi") {
        //            println("Foii")
        //        }
        //    }
        //}
        // TODO: Restante das features
    }

    override fun getItemCount(): Int {
        return featuresList.size
    }

    class FeaturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: View = itemView.findViewById(R.id.image_view)
        val labelView: TextView = itemView.findViewById(R.id.label_view)
        val paramView: TextView = itemView.findViewById(R.id.param_view)
        val tittleView: TextView = itemView.findViewById(R.id.tittle_view)
        val descriptionView: TextView = itemView.findViewById(R.id.description_view)
        val buttonText: Button = itemView.findViewById(R.id.button_text)
    }
}
