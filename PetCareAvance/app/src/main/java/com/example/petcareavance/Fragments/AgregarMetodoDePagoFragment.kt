package com.example.petcareavance.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petcareavance.R
import com.example.petcareavance.api.dataclasses.payment.AddPaymentResponse
import com.example.petcareavance.api.dataclasses.payment.PaymentResponse
import com.example.petcareavance.api.dataclasses.users.UserUpdate
import com.example.petcareavance.api.services.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgregarMetodoDePagoFragment :Fragment() {
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_payment_method, container, false)


        val btnAgregarMetodoDePago = view.findViewById<Button>(R.id.btnAgregarTarjeta)


        btnAgregarMetodoDePago.setOnClickListener {
            addPaymentMethod()
        }

        return view
    }

    fun addPaymentMethod(){

        // Obtener User ID de SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserID", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("ID", "") ?: ""

        // Obtener Token de SharedPreferences
        val sharedPreferences2 = requireActivity().getSharedPreferences("UserToken", Context.MODE_PRIVATE)
        val token = sharedPreferences2.getString("Token", "") ?: ""

        // toamr los valores -- 2023-10-19T20:35:43.764Z
        val name = view?.findViewById<EditText>(R.id.tvNombre)?.text.toString()
        val lastName = view?.findViewById<EditText>(R.id.etApellido)?.text.toString()
        var expiracion = view?.findViewById<EditText>(R.id.etVencimiento)?.text.toString()
        val cvc = view?.findViewById<EditText>(R.id.etCVC)?.text.toString().toIntOrNull()
        val numeroTarjeta = view?.findViewById<EditText>(R.id.tvNumeroTarjeta)?.text.toString()




        val addPaymentResponse = AddPaymentResponse(
             name= name,
             lastName= lastName,
             number= numeroTarjeta,
             expiratedDay= expiracion,
             cvv= cvc?:0,
             userId= userId.toInt()
        )


        val retrofit = Retrofit.Builder()
            .baseUrl("https://petcarebackend.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)




        val call = apiService.postPayment(  addPaymentResponse)
        call.enqueue(object : Callback<AddPaymentResponse> {
            override fun onResponse(
                call: Call<AddPaymentResponse>,
                response: Response<AddPaymentResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Tarjeta actualizado exitosamente", Toast.LENGTH_LONG).show()

                } else {
                    Log.e("ERRORRRRR", "Error Code: ${response.code()}, Error Message: ${response.message()}")
                    Log.d("ERRRRRRRRRRROR", "HTTP Status Code: ${response.code()}")
                    Log.d("HTTP Response", "Response: $response")

                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AddPaymentResponse>, t: Throwable) {
                Log.d("ERROR", "ERROR: $t.localizedMessage")
                Toast.makeText(requireContext(), "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }


        })
    }
}