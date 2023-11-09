package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R

class SoporteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_soporte, container, false)

        val textView: TextView = view.findViewById(R.id.tvPerfil)

        val sharedPreferences = activity?.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("ID", "No ID")

        textView.text = "Estás en Perfil\nEl ID actual es: $id"

        val btnIrAProceso = view.findViewById<TextView>(R.id.tvContact)
        btnIrAProceso.setOnClickListener {
            avanzar()
        }

        return view
    }

    fun avanzar() {
        val procesoPublicacionFragment = Contact()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}



