package com.robotec.caller.utils.speak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.R

import com.robotec.temi.speak.Voice

class SpeakAdapter(private val speakList : ArrayList<Speak>) :
    RecyclerView.Adapter<SpeakAdapter.SpeakViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_speak, parent, false)
        return SpeakViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SpeakViewHolder, position: Int) {

        val speak = Voice()

        val currentItem = speakList[position]
        holder.imageView.setBackgroundResource(currentItem.imageViewID)

        holder.labelView.text = currentItem.labelViewID
        holder.tittleView.text = currentItem.tittleViewID
        holder.descriptionView.text = currentItem.descriptionViewID
        holder.paramView.text = currentItem.paramViewID

        holder.buttonText.setOnClickListener {
            speak.wakeUp() {}
        }

        // ############################################################ //
    }

    override fun getItemCount(): Int {
        return speakList.size
    }

    class SpeakViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: View = itemView.findViewById(R.id.image_view)
        val labelView: TextView = itemView.findViewById(R.id.label_view)
        val paramView: TextView = itemView.findViewById(R.id.param_view)
        val tittleView: TextView = itemView.findViewById(R.id.tittle_view)
        val descriptionView: TextView = itemView.findViewById(R.id.description_view)
        val buttonText: Button = itemView.findViewById(R.id.button_text)
    }
}
