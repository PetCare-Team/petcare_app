package com.example.petcareavance

import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract.Colors
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        val btHour1= view.findViewById<Button>(R.id.btHour1)
        val btHour2= view.findViewById<Button>(R.id.btHour2)
        val btHour3= view.findViewById<Button>(R.id.btHour3)


        val btRetroceder= view.findViewById<ImageView>(R.id.ivReturn)

        btRetroceder.setOnClickListener(){

            retroceder()
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"

            Toast.makeText(context, "Fecha seleccionada: $selectedDate", Toast.LENGTH_SHORT).show()

           sharedViewModel.selectedDate = selectedDate


        }

        btHour1.setOnClickListener{

            val drawable = resources.getDrawable(R.drawable.btn_background)

            btHour1.background = drawable
            btHour2.setBackgroundColor(Color.GRAY)
            btHour3.setBackgroundColor(Color.GRAY)

            sharedViewModel.selectedHour = btHour1.text.toString()+":00"

            Toast.makeText(requireContext(), "${sharedViewModel.selectedHour}", Toast.LENGTH_LONG).show()
        }

        btHour2.setOnClickListener{

            val drawable = resources.getDrawable(R.drawable.btn_background)

            btHour2.background = drawable
            btHour1.setBackgroundColor(Color.GRAY)
            btHour3.setBackgroundColor(Color.GRAY)

            sharedViewModel.selectedHour = btHour2.text.toString()+":00"

            Toast.makeText(requireContext(), "${sharedViewModel.selectedHour}", Toast.LENGTH_LONG).show()


        }

        btHour3.setOnClickListener{

            val drawable = resources.getDrawable(R.drawable.btn_background)

            btHour3.background = drawable
            btHour2.setBackgroundColor(Color.GRAY)
            btHour1.setBackgroundColor(Color.GRAY)

            sharedViewModel.selectedHour = btHour3.text.toString()+":00"

            Toast.makeText(requireContext(), "${sharedViewModel.selectedHour}", Toast.LENGTH_LONG).show()


        }


        return view

    }

    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }

}