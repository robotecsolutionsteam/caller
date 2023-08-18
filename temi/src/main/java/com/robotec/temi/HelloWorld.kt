package com.robotec.temi

import android.content.Context
import android.widget.Toast

object Hello {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}