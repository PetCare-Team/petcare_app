package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.reservas.ReservaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MyServices : Fragment(){

    private var reservaDataItem: ReservaResponse? = null


    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_myservices, container, false)
        val btnretroceder = view.findViewById<ImageView>(R.id.imageView20)
        return view

//        val view = inflater.inflate(R.layout.fragment_myservices, container, false)
//        val btnretroceder = view.findViewById<ImageView>(R.id.imageView20)
//
//
//        val myParam: Int? = arguments?.getInt(ARG_PARAM)
//        Log.d("IMPORTANE2222:" , "$myParam")
//
//        val btnAvanzar = view.findViewById<ImageButton>(R.id.imageButton3)
//
//
//
//
//        btnretroceder.setOnClickListener {
//            retroceder()
//        }
//
//
//        Log.d("myParam","$myParam")
//
//        val call = RetrofitClient.instance.getReservaById(myParam.toString()) //////////////
//
//        call.enqueue(object : Callback<ReservaResponse> {
//            @SuppressLint("SetTextI18n")
//            override fun onResponse(
//                call: Call<ReservaResponse>,
//                response: Response<ReservaResponse>
//            ) {
//                reservaDataItem = response.body()
//
//                Log.d("API_Response", "Received service data: ${reservaDataItem}")
//
//
//
//                if (reservaDataItem != null) {
//                    Toast.makeText(requireContext(), "Nombre del servicio: ${reservaDataItem}", Toast.LENGTH_LONG).show()
//
//                    val direccion: TextView = view.findViewById(R.id.textView37)
//                    val hora: TextView = view.findViewById(R.id.textView38)
//                    val nroTarjeta: TextView = view.findViewById(R.id.textView21)
//                    val costo: TextView = view.findViewById(R.id.textView40)
//                    val adicionales: TextView = view.findViewById(R.id.textView41)
//                    val cuotaPetcare: TextView = view.findViewById(R.id.textView42)
//                    val totalPagado: TextView = view.findViewById(R.id.textView43)
//                    val nombreGrande: TextView = view.findViewById(R.id.textView32)
//                    val nombrePequeno: TextView = view.findViewById(R.id.textView29)
//
//
//
//                    direccion.text = reservaDataItem!!.serviceProvider.location
//                    hora.text = formatStartHour(reservaDataItem!!.startHour)  // convertir
//
//                    val number = reservaDataItem!!.clientPayment.number
//                    val lastFourDigits = if (number.length >= 4) {
//                        number.takeLast(4)
//                    } else {
//                        number
//                    }
//
//                    nroTarjeta.text = "*$lastFourDigits" // ultimos 4 digitos
//
//                    costo.text = "S/"+ reservaDataItem!!.serviceProvider.price.toString()+".00"
//                    adicionales.text = "S/0.00"
//                    val cuota = reservaDataItem!!.serviceProvider.price*0.20
//                    cuotaPetcare.text = "S/$cuota"+"0"
//                    val totalSuma = reservaDataItem!!.serviceProvider.price*1.20
//                    totalPagado.text = "S/$totalSuma"+"0"
//                    nombreGrande.text = reservaDataItem!!.serviceProvider.user.firstName
//                    nombrePequeno.text = reservaDataItem!!.serviceProvider.user.firstName
//
//
//                } else {
//                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
//                }
//            }
//
//
//
//            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
//                Log.d("asd","${t.message}")
//
//                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
//            }
//        })
//
//        btnretroceder.setOnClickListener {
//            retroceder()
//        }
//
//        btnAvanzar.setOnClickListener {
//            val myParam = reservaDataItem?.serviceProvider?.serviceId
//            avanzarRating(myParam?:0)
//
//
//        }
//        return view
}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnretroceder = view.findViewById<ImageView>(R.id.imageView20)
        val btnAvanzar = view.findViewById<ImageButton>(R.id.imageButton3)

        btnretroceder.setOnClickListener {
            retroceder()
        }

        btnAvanzar.setOnClickListener {
            val myParam = reservaDataItem?.serviceProvider?.serviceId
            avanzarRating(myParam ?: 0)
        }
    }

    override fun onResume() {
        super.onResume()

        val myParam: Int? = arguments?.getInt(ARG_PARAM)
        Log.d("IMPORTANE2222:", "$myParam")

        if (myParam != null) {
            fetchReservaData(myParam.toString())
        }
    }

    private fun fetchReservaData(reservaId: String) {
        val call = RetrofitClient.instance.getReservaById(reservaId)

        call.enqueue(object : Callback<ReservaResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ReservaResponse>,
                response: Response<ReservaResponse>
            ) {
                reservaDataItem = response.body()
                val view = view ?: return

                if (reservaDataItem != null) {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${reservaDataItem}", Toast.LENGTH_LONG).show()

                    val direccion: TextView = view.findViewById(R.id.servicelocation)
                    val hora: TextView = view.findViewById(R.id.servicedni)
                    val nroTarjeta: TextView = view.findViewById(R.id.textView21)
                    val costo: TextView = view.findViewById(R.id.textView40)
                    val adicionales: TextView = view.findViewById(R.id.textView41)
                    val cuotaPetcare: TextView = view.findViewById(R.id.textView42)
                    val totalPagado: TextView = view.findViewById(R.id.textView43)
                    val nombreGrande: TextView = view.findViewById(R.id.textView32)
                    val nombrePequeno: TextView = view.findViewById(R.id.textView29)

                    direccion.text = reservaDataItem!!.serviceProvider.location
                    hora.text = formatStartHour(reservaDataItem!!.startHour)  // convertir

                    val number = reservaDataItem!!.clientPayment.number
                    val lastFourDigits = if (number.length >= 4) {
                        number.takeLast(4)
                    } else {
                        number
                    }

                    nroTarjeta.text = "*$lastFourDigits" // ultimos 4 digitos

                    costo.text = "S/"+reservaDataItem!!.serviceProvider.price.toString()+".00"
                    adicionales.text = "S/0.00"
                    val cuota = reservaDataItem!!.serviceProvider.price*0.20
                    cuotaPetcare.text = "S/$cuota"+"0"
                    val totalSuma = reservaDataItem!!.serviceProvider.price*1.20
                    totalPagado.text = "S/$totalSuma"+"0"
                    nombreGrande.text = reservaDataItem!!.serviceProvider.user.firstName
                    nombrePequeno.text = reservaDataItem!!.serviceProvider.user.firstName

                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ReservaResponse>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }


    fun formatStartHour(startHour: String): String {
        // Paso 1: Parsear la cadena a LocalDateTime
        val dateTime = LocalDateTime.parse(startHour, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // Paso 2: Formatear el objeto LocalDateTime a la cadena deseada
        val formatter = DateTimeFormatter.ofPattern("dd/MM")
        return dateTime.format(formatter)
    }

    fun avanzarRating(myParam:Int){
        val serviceInfo = Rating().apply {
            arguments = Bundle().apply {
                putInt("myParamKey",  myParam)
            }
        }
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, serviceInfo)
        transaction.addToBackStack(null)
        transaction.commit()
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