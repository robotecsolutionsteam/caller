package com.robotec.caller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(var context: Context, var CardName: Array<String>) :
    BaseAdapter() {
    var inflater: LayoutInflater? = null
    override fun getCount(): Int {
        return CardName.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater!!.inflate(R.layout.grid_item, parent, false)
        }
        if (inflater == null) inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val textView = convertView?.findViewById<TextView>(R.id.grid_text)
        textView?.text = CardName[position]
        return convertView!!
    }
}