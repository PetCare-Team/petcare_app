package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnyServiceFragment: Fragment() {

    val ARG_SERVICE_ID: String = "0"


    companion object {
        const val ARG_SERVICE_ID = "serviceId"
    }

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anyservice, container, false)

        val transaction = requireFragmentManager()
        val btnRetroceder = view.findViewById<ImageButton>(R.id.imageView20)

        btnRetroceder.setOnClickListener {
            retroceder()
        }

        val btnServiceConfirm = view.findViewById<Button>(R.id.btnreservarservice)

        btnServiceConfirm.setOnClickListener{

            avanzarServicio(ARG_SERVICE_ID,transaction)

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

                    val servicelocation: TextView = view.findViewById(R.id.servicelocation)
                    val servicename: TextView = view.findViewById(R.id.servicename)
                    val servicedni: TextView = view.findViewById(R.id.servicedni)
                    val servicedescription: TextView = view.findViewById(R.id.servicedescription)
                   // val serviceprice: TextView = view.findViewById(R.id.tvServicePrice)

                    servicename.text = "AGREGA A TUS ENTOITIS LA OPCION PARA ACCEDER A USER"
                    servicelocation.text = "Vivo en ${serviceDataItem!!.location}"
                    servicedni.text = "${serviceDataItem!!.dni}"
                    servicedescription.text = "${serviceDataItem!!.description}"
                   // serviceprice.text = "S/${serviceDataItem!!.price}.00 por dia"

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

    private fun avanzarServicio(serviceId: String,transaction: FragmentManager) {
        val serviceFragment = ConfirmAnyServiceFragment()
        val bundle = Bundle()
        bundle.putString(this.ARG_SERVICE_ID, serviceId)

        serviceFragment.arguments = bundle

        val transaction = transaction.beginTransaction()
        transaction.replace(R.id.fragment_container, serviceFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}