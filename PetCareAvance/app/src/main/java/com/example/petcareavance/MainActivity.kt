package com.example.petcareavance

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petcareavance.Fragments.LogInFragment
import com.example.petcareavance.Fragments.SignUpFragment
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.UserInfo
import com.example.petcareavance.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.mainLogIn)
        val btnSignUp = findViewById<Button>(R.id.mainSignUp)
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)

        btnLogin.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LogInFragment())
                .commit()
            fragmentContainer.bringToFront() // Mueve el FrameLayout al frente
            btnLogin.visibility = View.GONE // Esconde el botón de login
            btnSignUp.visibility = View.GONE // Esconde el botón de signup
        }

        btnSignUp.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignUpFragment())
                .commit()
            fragmentContainer.bringToFront() // Mueve el FrameLayout al frente
            btnLogin.visibility = View.GONE // Esconde el botón de login
            btnSignUp.visibility = View.GONE // Esconde el botón de signup
        }
    }
}
