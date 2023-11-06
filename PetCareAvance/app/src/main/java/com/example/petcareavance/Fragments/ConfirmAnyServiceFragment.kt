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
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmAnyServiceFragment: Fragment() {

    val ARG_SERVICE_ID: String = "0"

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmservice, container, false)


        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView20)

        btnRetroceder.setOnClickListener {
            retroceder()
        }

         var serviceDataItem: ServiceResponse? = null

        val call = RetrofitClient.instance.getServiceById(ARG_SERVICE_ID) // Colocar id pro params

        call.enqueue(object : Callback<ServiceResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                serviceDataItem = response.body()
                val view = view ?: return

                if (serviceDataItem != null) {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${serviceDataItem}", Toast.LENGTH_LONG).show()

                    val servicelocation: TextView = view.findViewById(R.id.tvLocation)
                    val servicename: TextView = view.findViewById(R.id.tvNameService)
                    val serviceprice: TextView = view.findViewById(R.id.serviceprice)

                    servicename.text = "${serviceDataItem!!.user.firstName}"
                    servicelocation.text = "Vivo en ${serviceDataItem!!.location}"

                    serviceprice.text = "S/${serviceDataItem!!.price}.00 por dia"

                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })


        return view;
    }


    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }
}