package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.petcareavance.R

class SoporteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_soporte, container, false)

        val btnAvanzar = view.findViewById<TextView>(R.id.textView3)

        btnAvanzar.setOnClickListener {
            avanzar()
        }

        return view
    }

    private fun avanzar() {
        val editPerfilFragment = EditPerfil()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, editPerfilFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
