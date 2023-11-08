package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.petcareavance.R

class confirmReclamo: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmrese, container, false)


        val btnIrAProceso2 = view.findViewById<ImageButton>(R.id.imageButton2)
        btnIrAProceso2.setOnClickListener {
            retroceder()
        }

        return view
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