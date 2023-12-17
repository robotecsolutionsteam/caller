package com.robotec.caller.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.robotec.caller.R
import com.robotec.caller.view.NavigationActivity

class CardAdapter(private val cardboardList : ArrayList<Cardboard>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.custom_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        val currentItem = cardboardList[position]
        holder.imageView.setBackgroundResource(currentItem.imageViewID)
        holder.textView.text = currentItem.textViewID

        // ############################################################ //

        holder.imageView.setOnClickListener {
            val intent = Intent(it.context, NavigationActivity::class.java)
            it.context.startActivity(intent)
        }

        // TODO: Restante das features
    }

    override fun getItemCount(): Int {
        return cardboardList.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: CardView = itemView.findViewById(R.id.image_view)
        val textView: TextView = itemView.findViewById(R.id.text_view)
    }
}