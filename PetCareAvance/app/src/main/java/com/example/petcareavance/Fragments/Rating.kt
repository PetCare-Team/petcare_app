package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.petcareavance.R

class Rating: Fragment() {
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)

        val btnAvanzar = view.findViewById<Button>(R.id.btnService4)

        btnAvanzar.setOnClickListener {
            avanzar()
        }
        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView13)

        btnRetroceder.setOnClickListener {
            retroceder()
        }
        return view;
    }

    private fun avanzar(){
        val serviceInfo = confirmReclamo()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun retroceder(){
        val serviceInfo = MyServices()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}