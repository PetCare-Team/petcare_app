package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R

class Contact:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        val btnIrAProceso = view.findViewById<Button>(R.id.button4)
        btnIrAProceso.setOnClickListener {
            avanzar2()
        }
        val btnIrAProceso2 = view.findViewById<ImageButton>(R.id.imageButton4)
        btnIrAProceso2.setOnClickListener {
            retroceder()
        }

        return view
    }

    fun avanzar2() {
        val procesoPublicacionFragment = confirmReclamo()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun retroceder() {
        parentFragmentManager.popBackStack()

//        val procesoPublicacionFragment = RecentQuestions()
//        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }
}
