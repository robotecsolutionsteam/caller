package com.robotec.temi

import android.content.Context
import android.widget.Toast

object Hello {
    fun sayHello() {
        println("Hello, world!")
    }
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    fun Sum(var1: Int, var2: Int) {
        val sum = var1 + var2
        return sum
    }
}