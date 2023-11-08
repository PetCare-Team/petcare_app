package com.example.petcareavance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.petcareavance.views.SharedViewModel

class EditDateFragment : Fragment() {


    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_edit_date, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val calendar= view.findViewById<CalendarView>(R.id.calendarView)


        val btRetroceder= view.findViewById<ImageView>(R.id.ivReturn)

        btRetroceder.setOnClickListener(){

            retroceder()
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"

            Toast.makeText(context, "Fecha seleccionada: $selectedDate", Toast.LENGTH_SHORT).show()

           sharedViewModel.selectedDate = selectedDate


        }

        return view

    }

    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }

}