package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.helpquestion.HelpQuestionBody
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Contact:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val btnGuardar = view.findViewById<Button>(R.id.button4)
        btnGuardar.setOnClickListener {
            val title = view.findViewById<EditText>(R.id.etTitlle).text.toString()
            val question = view.findViewById<EditText>(R.id.etQuestion).text.toString()
            val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
            val userIdString = sharedPreferences.getString("ID", "") ?: ""
            val userId = userIdString.toInt()
            postHelpQuestion(title, question, userId)
            avanzar2()
        }
        val btnIrAProceso2 = view.findViewById<ImageButton>(R.id.imageButton4)
        btnIrAProceso2.setOnClickListener {
            retroceder()
        }

        return view
    }

    private fun postHelpQuestion(title: String, question: String, userId: Int) {
        // Configura Retrofit para hacer la llamada POST
        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Crea el cuerpo de la solicitud
        val body = HelpQuestionBody(title, question, userId)

        // Realiza la llamada POST
        apiService.postHelpQuestion(body).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Maneja la respuesta exitosa
                    Toast.makeText(requireContext(), "Question submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    // Maneja la respuesta de error
                    Toast.makeText(requireContext(), "Failed to submit question", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Maneja el error de la llamada
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun avanzar2() {
        val procesoPublicacionFragment = confirmReclamo()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun retroceder() {
        val procesoPublicacionFragment = SoporteFragment()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}