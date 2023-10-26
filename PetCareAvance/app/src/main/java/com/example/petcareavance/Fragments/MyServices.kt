package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyServices : Fragment(){

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myservices, container, false)
        val btnretroceder = view.findViewById<ImageView>(R.id.imageView20)


        val myParam: Int? = arguments?.getInt(ARG_PARAM)

        Log.d("myParam","$myParam")

        val call = RetrofitClient.instance.getReservaById(myParam.toString()) //////////////

        call.enqueue(object : Callback<ReservaResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ReservaResponse>,
                response: Response<ReservaResponse>
            ) {
                val reservaDataItem = response.body()

                Log.d("API_Response", "Received service data: ${reservaDataItem}")



                if (reservaDataItem != null) {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${reservaDataItem}", Toast.LENGTH_LONG).show()

                    val direccion: TextView = view.findViewById(R.id.textView37)
                    val hora: TextView = view.findViewById(R.id.textView38)
                    val nroTarjeta: TextView = view.findViewById(R.id.textView21)
                    val costo: TextView = view.findViewById(R.id.textView40)
                    val adicionales: TextView = view.findViewById(R.id.textView41)
                    val cuotaPetcare: TextView = view.findViewById(R.id.textView42)
                    val totalPagado: TextView = view.findViewById(R.id.textView43)
                    val nombreGrande: TextView = view.findViewById(R.id.textView32)
                    val nombrePequeno: TextView = view.findViewById(R.id.textView29)



                    direccion.text = reservaDataItem.serviceProvider.location
                    hora.text = formatStartHour(reservaDataItem.startHour)  // convertir

                    val number = reservaDataItem.clientPayment.number
                    val lastFourDigits = if (number.length >= 4) {
                        number.takeLast(4)
                    } else {
                        number
                    }

                    nroTarjeta.text = "*$lastFourDigits" // ultimos 4 digitos

                    costo.text = "S/"+reservaDataItem.serviceProvider.price.toString()+".00"
                    adicionales.text = "S/0.00"
                    val cuota = reservaDataItem.serviceProvider.price*0.20
                    cuotaPetcare.text = "S/$cuota"+"0"
                    val totalSuma = reservaDataItem.serviceProvider.price*1.20
                    totalPagado.text = "S/$totalSuma"+"0"
                    nombreGrande.text = reservaDataItem.serviceProvider.user.firstName
                    nombrePequeno.text = reservaDataItem.serviceProvider.user.firstName



//                    textView37 - direccion
//                    textView38 - inicio hora
//                    textView21 - nro tarjeta
//                    textView40 -costo
//                    textView32, textView29 - nombre provider
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }



            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
                Log.d("asd","${t.message}")

                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        btnretroceder.setOnClickListener {
            retroceder()
        }
        return view
}
    fun formatStartHour(startHour: String): String {
        // Paso 1: Parsear la cadena a LocalDateTime
        val dateTime = LocalDateTime.parse(startHour, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Paso 2: Formatear el objeto LocalDateTime a la cadena deseada
        val formatter = DateTimeFormatter.ofPattern("dd/MM")
        return dateTime.format(formatter)
    }
    companion object {
        private const val ARG_PARAM = "some_param"

        fun newInstance(param: Int): MyServices {
            val fragment = MyServices()
            val args = Bundle()
            args.putInt(ARG_PARAM, param)
            fragment.arguments = args
            return fragment
        }
    }

    private fun retroceder() {

        val serviceInfo = ServicesDataFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}