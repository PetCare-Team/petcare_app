package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.petcareavance.R

class EditPerfil : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_perfil, container, false)

        val btnRetroceder = view.findViewById<ImageView>(R.id.imageButton)


        btnRetroceder.setOnClickListener {
            retroceder()
        }



        return view
    }

    private fun retroceder() {
            val publicarFragment = SoporteFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, publicarFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}