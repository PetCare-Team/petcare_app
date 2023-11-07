package com.example.petcareavance.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.RetrofitClient
import com.example.petcareavance.api.dataclasses.pets.PetResponse
import com.example.petcareavance.api.dataclasses.services.ServiceResponse
import com.example.petcareavance.editPet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConfirmAnyServiceFragment: Fragment() {




    companion object {
        const val ARG_SERVICE_ID = "serviceId"
    }


    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirmservice, container, false)

        val serviceId=arguments?.getString(ARG_SERVICE_ID, "-1")!!

        val btnRetroceder = view.findViewById<ImageView>(R.id.imageView20)

        btnRetroceder.setOnClickListener {
            retroceder()
        }

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""


        val call2 = RetrofitClient.instance.getPetByUser(token, userId) // Colocar id pro params

        var petDataItems: List<PetResponse>? = null

        call2.enqueue(object : Callback<List<PetResponse>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<PetResponse>>,
                response: Response<List<PetResponse>>
            ) {
                petDataItems = response.body()
                val view = view ?: return

                if (petDataItems != null) {
                    Log.d("WWWW", "$petDataItems")

                    val amscotaValue:TextView = view.findViewById(R.id.tvMascotaSelect)

                    amscotaValue.text = "${petDataItems!![0].name}"


                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<PetResponse>>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })






        var precio=0.00
        var cuota=0.00
        val cuotaText= view.findViewById<TextView>(R.id.tvCuota)


        var serviceDataItem: ServiceResponse? = null

        val call = RetrofitClient.instance.getServiceById(serviceId) // Colocar id pro params

        call.enqueue(object : Callback<ServiceResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ServiceResponse>,
                response: Response<ServiceResponse>
            ) {
                serviceDataItem = response.body()
                val view = view ?: return

                if (serviceDataItem != null) {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${serviceId}", Toast.LENGTH_LONG).show()

                    val servicelocation: TextView = view.findViewById(R.id.tvLocation)
                    val servicename: TextView = view.findViewById(R.id.tvNameService)
                    val serviceprice: TextView = view.findViewById(R.id.serviceprice)

                    servicename.text = "${serviceDataItem!!.user.firstName}"
                    servicelocation.text = "Vivo en ${serviceDataItem!!.location}"

                    serviceprice.text = "S/${serviceDataItem!!.price}.00 por dia"
                    precio=serviceDataItem!!.price.toDouble()

                    cuota=(precio*0.2).toDouble()
                    cuotaText.setText("S/${cuota}")

                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener el servicio", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ServiceResponse>, t: Throwable) {
                Log.d("asd", "${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        val radioGroup = view.findViewById<RadioGroup>(R.id.rgServiceType)
        val adicional= view.findViewById<TextView>(R.id.tvAdicional)
        val total= view.findViewById<TextView>(R.id.tvTotal)


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbPremium-> {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${cuota}", Toast.LENGTH_LONG).show()

                    adicional.setText("S/20.00")
                    total.setText("S/"+(cuota+20+precio))

                }
                R.id.rbBasic -> {
                    Toast.makeText(requireContext(), "Nombre del servicio: ${cuota}", Toast.LENGTH_LONG).show()

                    adicional.setText("S/0.00")
                    total.setText("S/"+(cuota+precio))
                }

            }
        }



        return view;
    }


    private fun retroceder(){
        parentFragmentManager.popBackStack()


    }
}