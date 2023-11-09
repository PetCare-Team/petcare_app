// PublicarFragment.kt

package com.example.petcareavance.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.petcareavance.R

class PublicarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_publicar, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spin1)
        val spinner2 = view.findViewById<Spinner>(R.id.snrHour)

        val opciones = listOf("Dias disponible", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
        val opciones2 = listOf("Horas disponible", "2pm", "8pm", "7am")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones2)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner2.adapter = adapter2

        spinner.setSelection(0)
        spinner2.setSelection(0)

        // Obtén una referencia al TextView al que deseas cambiar el color del borde
        val textView = view.findViewById<TextView>(R.id.tvDirecc)
        val textView2 = view.findViewById<TextView>(R.id.tmDescription)

        // Asociar la acción al botón
        val btnIrAProceso = view.findViewById<Button>(R.id.btnService)
        btnIrAProceso.setOnClickListener {
            irAProcesoPublicacion()
        }

        val btnRetroceder= view.findViewById<ImageView>(R.id.ivReturn2)

        btnRetroceder.setOnClickListener{

            retroceder()
        }

        return view
    }

    fun irAProcesoPublicacion() {
        val procesoPublicacionFragment = ProcesoPublicacion()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, procesoPublicacionFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun retroceder(){
        val inicioFragment = InicioFragment()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, inicioFragment)
        transaction.addToBackStack(null)
        transaction.commit()


    }
}
