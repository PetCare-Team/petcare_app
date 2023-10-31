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

class confirmReclamo: Fragment() {

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmrese, container, false)


        val btnRetroceder = view.findViewById<ImageButton>(R.id.imageButton2)

        btnRetroceder.setOnClickListener {
            retroceder()
        }
        return view;
    }


    private fun retroceder(){
        val serviceInfo = Rating()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}