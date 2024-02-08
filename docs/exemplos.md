---
outline: deep
---

# Exemplos de Usos

## Navegação 

Exemplo de uso do `goto` para um local qualquer.

```kotlin 
package com.robotec.caller

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.robotec.temi.navigation.Navigation
import com.robotec.caller.databinding.ActivityMainBinding

class MainActivity : ComponentActivity(){

    private lateinit var binding: ActivityMainBinding
    private val nav: Navigation = Navigation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nav.goTo("x6") {}

    }
}
```