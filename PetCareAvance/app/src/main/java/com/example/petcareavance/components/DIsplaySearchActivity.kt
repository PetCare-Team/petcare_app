package com.example.petcareavance.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.petcareavance.R

class DIsplaySearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_search)

        val texto= findViewById<TextView>(R.id.textView)

        val fecha= intent.getStringExtra("FECHA")

        texto.text= fecha


    }
}