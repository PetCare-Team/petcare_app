package com.example.petcareavance.Fragments

import RecycleViewFragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.petcareavance.R
import com.example.petcareavance.views.SharedViewModel

//import com.example.petcareavance.recicle_view // Asegúrate de que esta importación sea correcta

class InicioFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_inicio, container, false)
        val calendar = view.findViewById<CalendarView>(R.id.cvCalendario)
        val fecha = view.findViewById<EditText>(R.id.etDate)
        val direccion = view.findViewById<EditText>(R.id.etAddress)
        val btBuscar = view.findViewById<Button>(R.id.btBuscar)

        val sharedPreferences = activity?.getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("ID", "No ID")

        calendar.visibility = View.INVISIBLE

        fecha.setOnClickListener {
            calendar.visibility = View.VISIBLE
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            fecha.setText(selectedDate)
            Toast.makeText(context, "Fecha seleccionada: $selectedDate", Toast.LENGTH_SHORT).show()
            calendar.visibility = View.INVISIBLE
            sharedViewModel.selectedDate = selectedDate


        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val fechaseleccionada = fecha.text.isNotBlank()
                val direccionseleccionada = direccion.text.isNotBlank()
                btBuscar.isEnabled = direccionseleccionada
            }
        }

        fecha.addTextChangedListener(textWatcher)
        direccion.addTextChangedListener(textWatcher)

        btBuscar.setOnClickListener {
            // Reemplaza el fragmento actual por RecycleViewFragment
            val transaction = parentFragmentManager.beginTransaction()
            val newFragment = RecycleViewFragment().apply {
                arguments = Bundle().apply {
                    putString("FECHA", fecha.text.toString())
                    putString("DIRECCION", direccion.text.toString())
                }
            }
            transaction.replace(R.id.fragment_container, newFragment) // Asegúrate de usar el ID correcto del contenedor principal
            transaction.addToBackStack(null) // Para que el usuario pueda volver al fragmento anterior
            transaction.commit()
        }

        return view
    }
}
