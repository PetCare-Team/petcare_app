package com.example.petcareavance.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R

class ProcesoPublicacion : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_proceso_publicacion, container, false)

        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView)
        val btnAvanzar = view.findViewById<Button>(R.id.button)

        btnRetroceder.setOnClickListener {
            retroceder()
        }

        btnAvanzar.setOnClickListener {
            avanzar()
        }

        return view
    }

    private fun retroceder() {
        val publicarFragment = PublicarFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, publicarFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun avanzar() {

        val publicarServiceFragment = PublicarService()
         val transaction = requireFragmentManager().beginTransaction()
         transaction.replace(R.id.fragment_container, publicarServiceFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
