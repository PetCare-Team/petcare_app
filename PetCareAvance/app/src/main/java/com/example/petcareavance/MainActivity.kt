package com.example.petcareavance

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextID = findViewById<EditText>(R.id.etID)
        val btnNavigate = findViewById<Button>(R.id.btnNavigate)

        btnNavigate.setOnClickListener(View.OnClickListener {
            val id: String = editTextID.getText().toString()
            if (!id.isEmpty()) {
                val sharedPreferences = getSharedPreferences("UserID", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("ID", id)
                editor.apply()
                val intent = Intent(this@MainActivity, MenuActivity::class.java)
                startActivity(intent)
            }
        })
    }
}