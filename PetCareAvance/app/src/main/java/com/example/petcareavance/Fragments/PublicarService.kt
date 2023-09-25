package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.petcareavance.R

class PublicarService: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.publicar_service, container, false)

        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView3)

        btnRetroceder.setOnClickListener {
            retroceder()
        }


        val spinner = view.findViewById<Spinner>(R.id.Spinday)
        val spinner2 = view.findViewById<Spinner>(R.id.snrHour2)

        val opciones = listOf("Dias disponible", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val opciones2 = listOf("Horas disponible", "2pm", "8pm", "7am")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones2)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner2.adapter = adapter2

        spinner.setSelection(0)
        spinner2.setSelection(0)




        return view
    }

    private fun retroceder() {
        val publicarFragment = ProcesoPublicacion()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, publicarFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}